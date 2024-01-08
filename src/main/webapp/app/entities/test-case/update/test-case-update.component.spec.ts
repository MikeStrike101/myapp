import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TestCaseFormService } from './test-case-form.service';
import { TestCaseService } from '../service/test-case.service';
import { ITestCase } from '../test-case.model';
import { IProblem } from 'app/entities/problem/problem.model';
import { ProblemService } from 'app/entities/problem/service/problem.service';

import { TestCaseUpdateComponent } from './test-case-update.component';

describe('TestCase Management Update Component', () => {
  let comp: TestCaseUpdateComponent;
  let fixture: ComponentFixture<TestCaseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let testCaseFormService: TestCaseFormService;
  let testCaseService: TestCaseService;
  let problemService: ProblemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TestCaseUpdateComponent],
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
      .overrideTemplate(TestCaseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TestCaseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    testCaseFormService = TestBed.inject(TestCaseFormService);
    testCaseService = TestBed.inject(TestCaseService);
    problemService = TestBed.inject(ProblemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Problem query and add missing value', () => {
      const testCase: ITestCase = { id: 456 };
      const problem: IProblem = { id: 4343 };
      testCase.problem = problem;

      const problemCollection: IProblem[] = [{ id: 79339 }];
      jest.spyOn(problemService, 'query').mockReturnValue(of(new HttpResponse({ body: problemCollection })));
      const additionalProblems = [problem];
      const expectedCollection: IProblem[] = [...additionalProblems, ...problemCollection];
      jest.spyOn(problemService, 'addProblemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ testCase });
      comp.ngOnInit();

      expect(problemService.query).toHaveBeenCalled();
      expect(problemService.addProblemToCollectionIfMissing).toHaveBeenCalledWith(
        problemCollection,
        ...additionalProblems.map(expect.objectContaining)
      );
      expect(comp.problemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const testCase: ITestCase = { id: 456 };
      const problem: IProblem = { id: 68366 };
      testCase.problem = problem;

      activatedRoute.data = of({ testCase });
      comp.ngOnInit();

      expect(comp.problemsSharedCollection).toContain(problem);
      expect(comp.testCase).toEqual(testCase);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestCase>>();
      const testCase = { id: 123 };
      jest.spyOn(testCaseFormService, 'getTestCase').mockReturnValue(testCase);
      jest.spyOn(testCaseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testCase });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testCase }));
      saveSubject.complete();

      // THEN
      expect(testCaseFormService.getTestCase).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(testCaseService.update).toHaveBeenCalledWith(expect.objectContaining(testCase));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestCase>>();
      const testCase = { id: 123 };
      jest.spyOn(testCaseFormService, 'getTestCase').mockReturnValue({ id: null });
      jest.spyOn(testCaseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testCase: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testCase }));
      saveSubject.complete();

      // THEN
      expect(testCaseFormService.getTestCase).toHaveBeenCalled();
      expect(testCaseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestCase>>();
      const testCase = { id: 123 };
      jest.spyOn(testCaseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testCase });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(testCaseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProblem', () => {
      it('Should forward to problemService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(problemService, 'compareProblem');
        comp.compareProblem(entity, entity2);
        expect(problemService.compareProblem).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
