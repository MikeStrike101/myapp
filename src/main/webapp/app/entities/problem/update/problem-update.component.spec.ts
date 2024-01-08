import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProblemFormService } from './problem-form.service';
import { ProblemService } from '../service/problem.service';
import { IProblem } from '../problem.model';

import { ProblemUpdateComponent } from './problem-update.component';

describe('Problem Management Update Component', () => {
  let comp: ProblemUpdateComponent;
  let fixture: ComponentFixture<ProblemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let problemFormService: ProblemFormService;
  let problemService: ProblemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProblemUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProblemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProblemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    problemFormService = TestBed.inject(ProblemFormService);
    problemService = TestBed.inject(ProblemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const problem: IProblem = { id: 456 };

      activatedRoute.data = of({ problem });
      comp.ngOnInit();

      expect(comp.problem).toEqual(problem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProblem>>();
      const problem = { id: 123 };
      jest.spyOn(problemFormService, 'getProblem').mockReturnValue(problem);
      jest.spyOn(problemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ problem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: problem }));
      saveSubject.complete();

      // THEN
      expect(problemFormService.getProblem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(problemService.update).toHaveBeenCalledWith(expect.objectContaining(problem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProblem>>();
      const problem = { id: 123 };
      jest.spyOn(problemFormService, 'getProblem').mockReturnValue({ id: null });
      jest.spyOn(problemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ problem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: problem }));
      saveSubject.complete();

      // THEN
      expect(problemFormService.getProblem).toHaveBeenCalled();
      expect(problemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProblem>>();
      const problem = { id: 123 };
      jest.spyOn(problemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ problem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(problemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
