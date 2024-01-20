import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ExecutionCodeFormService } from './execution-code-form.service';
import { ExecutionCodeService } from '../service/execution-code.service';
import { IExecutionCode } from '../execution-code.model';

import { ExecutionCodeUpdateComponent } from './execution-code-update.component';

describe('ExecutionCode Management Update Component', () => {
  let comp: ExecutionCodeUpdateComponent;
  let fixture: ComponentFixture<ExecutionCodeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let executionCodeFormService: ExecutionCodeFormService;
  let executionCodeService: ExecutionCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ExecutionCodeUpdateComponent],
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
      .overrideTemplate(ExecutionCodeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExecutionCodeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    executionCodeFormService = TestBed.inject(ExecutionCodeFormService);
    executionCodeService = TestBed.inject(ExecutionCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const executionCode: IExecutionCode = { id: 456 };

      activatedRoute.data = of({ executionCode });
      comp.ngOnInit();

      expect(comp.executionCode).toEqual(executionCode);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExecutionCode>>();
      const executionCode = { id: 123 };
      jest.spyOn(executionCodeFormService, 'getExecutionCode').mockReturnValue(executionCode);
      jest.spyOn(executionCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ executionCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: executionCode }));
      saveSubject.complete();

      // THEN
      expect(executionCodeFormService.getExecutionCode).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(executionCodeService.update).toHaveBeenCalledWith(expect.objectContaining(executionCode));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExecutionCode>>();
      const executionCode = { id: 123 };
      jest.spyOn(executionCodeFormService, 'getExecutionCode').mockReturnValue({ id: null });
      jest.spyOn(executionCodeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ executionCode: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: executionCode }));
      saveSubject.complete();

      // THEN
      expect(executionCodeFormService.getExecutionCode).toHaveBeenCalled();
      expect(executionCodeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExecutionCode>>();
      const executionCode = { id: 123 };
      jest.spyOn(executionCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ executionCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(executionCodeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
