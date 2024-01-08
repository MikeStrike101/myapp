import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../user-problem.test-samples';

import { UserProblemFormService } from './user-problem-form.service';

describe('UserProblem Form Service', () => {
  let service: UserProblemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserProblemFormService);
  });

  describe('Service methods', () => {
    describe('createUserProblemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserProblemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            solvedAt: expect.any(Object),
            passedTestCases: expect.any(Object),
            xpAwarded: expect.any(Object),
            user: expect.any(Object),
            problem: expect.any(Object),
          })
        );
      });

      it('passing IUserProblem should create a new form with FormGroup', () => {
        const formGroup = service.createUserProblemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            solvedAt: expect.any(Object),
            passedTestCases: expect.any(Object),
            xpAwarded: expect.any(Object),
            user: expect.any(Object),
            problem: expect.any(Object),
          })
        );
      });
    });

    describe('getUserProblem', () => {
      it('should return NewUserProblem for default UserProblem initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createUserProblemFormGroup(sampleWithNewData);

        const userProblem = service.getUserProblem(formGroup) as any;

        expect(userProblem).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserProblem for empty UserProblem initial value', () => {
        const formGroup = service.createUserProblemFormGroup();

        const userProblem = service.getUserProblem(formGroup) as any;

        expect(userProblem).toMatchObject({});
      });

      it('should return IUserProblem', () => {
        const formGroup = service.createUserProblemFormGroup(sampleWithRequiredData);

        const userProblem = service.getUserProblem(formGroup) as any;

        expect(userProblem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserProblem should not enable id FormControl', () => {
        const formGroup = service.createUserProblemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserProblem should disable id FormControl', () => {
        const formGroup = service.createUserProblemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
