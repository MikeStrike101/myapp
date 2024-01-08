import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UserProblemFormService } from './user-problem-form.service';
import { UserProblemService } from '../service/user-problem.service';
import { IUserProblem } from '../user-problem.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProblem } from 'app/entities/problem/problem.model';
import { ProblemService } from 'app/entities/problem/service/problem.service';

import { UserProblemUpdateComponent } from './user-problem-update.component';

describe('UserProblem Management Update Component', () => {
  let comp: UserProblemUpdateComponent;
  let fixture: ComponentFixture<UserProblemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userProblemFormService: UserProblemFormService;
  let userProblemService: UserProblemService;
  let userService: UserService;
  let problemService: ProblemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UserProblemUpdateComponent],
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
      .overrideTemplate(UserProblemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserProblemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userProblemFormService = TestBed.inject(UserProblemFormService);
    userProblemService = TestBed.inject(UserProblemService);
    userService = TestBed.inject(UserService);
    problemService = TestBed.inject(ProblemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const userProblem: IUserProblem = { id: 456 };
      const user: IUser = { id: '5da39945-4de7-44e6-a156-f198a0955ed4' };
      userProblem.user = user;

      const userCollection: IUser[] = [{ id: 'fdede11a-2528-4f49-a4b0-bb22ba7f6b89' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userProblem });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Problem query and add missing value', () => {
      const userProblem: IUserProblem = { id: 456 };
      const problem: IProblem = { id: 63259 };
      userProblem.problem = problem;

      const problemCollection: IProblem[] = [{ id: 51346 }];
      jest.spyOn(problemService, 'query').mockReturnValue(of(new HttpResponse({ body: problemCollection })));
      const additionalProblems = [problem];
      const expectedCollection: IProblem[] = [...additionalProblems, ...problemCollection];
      jest.spyOn(problemService, 'addProblemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userProblem });
      comp.ngOnInit();

      expect(problemService.query).toHaveBeenCalled();
      expect(problemService.addProblemToCollectionIfMissing).toHaveBeenCalledWith(
        problemCollection,
        ...additionalProblems.map(expect.objectContaining)
      );
      expect(comp.problemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userProblem: IUserProblem = { id: 456 };
      const user: IUser = { id: '1357a2ba-5ec9-4261-a713-c52f067439e9' };
      userProblem.user = user;
      const problem: IProblem = { id: 47391 };
      userProblem.problem = problem;

      activatedRoute.data = of({ userProblem });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.problemsSharedCollection).toContain(problem);
      expect(comp.userProblem).toEqual(userProblem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserProblem>>();
      const userProblem = { id: 123 };
      jest.spyOn(userProblemFormService, 'getUserProblem').mockReturnValue(userProblem);
      jest.spyOn(userProblemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userProblem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userProblem }));
      saveSubject.complete();

      // THEN
      expect(userProblemFormService.getUserProblem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userProblemService.update).toHaveBeenCalledWith(expect.objectContaining(userProblem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserProblem>>();
      const userProblem = { id: 123 };
      jest.spyOn(userProblemFormService, 'getUserProblem').mockReturnValue({ id: null });
      jest.spyOn(userProblemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userProblem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userProblem }));
      saveSubject.complete();

      // THEN
      expect(userProblemFormService.getUserProblem).toHaveBeenCalled();
      expect(userProblemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserProblem>>();
      const userProblem = { id: 123 };
      jest.spyOn(userProblemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userProblem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userProblemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
