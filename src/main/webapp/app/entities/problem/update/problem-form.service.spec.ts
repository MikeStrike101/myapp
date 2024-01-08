import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../problem.test-samples';

import { ProblemFormService } from './problem-form.service';

describe('Problem Form Service', () => {
  let service: ProblemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProblemFormService);
  });

  describe('Service methods', () => {
    describe('createProblemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProblemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            level: expect.any(Object),
            xpReward: expect.any(Object),
          })
        );
      });

      it('passing IProblem should create a new form with FormGroup', () => {
        const formGroup = service.createProblemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            level: expect.any(Object),
            xpReward: expect.any(Object),
          })
        );
      });
    });

    describe('getProblem', () => {
      it('should return NewProblem for default Problem initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createProblemFormGroup(sampleWithNewData);

        const problem = service.getProblem(formGroup) as any;

        expect(problem).toMatchObject(sampleWithNewData);
      });

      it('should return NewProblem for empty Problem initial value', () => {
        const formGroup = service.createProblemFormGroup();

        const problem = service.getProblem(formGroup) as any;

        expect(problem).toMatchObject({});
      });

      it('should return IProblem', () => {
        const formGroup = service.createProblemFormGroup(sampleWithRequiredData);

        const problem = service.getProblem(formGroup) as any;

        expect(problem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProblem should not enable id FormControl', () => {
        const formGroup = service.createProblemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProblem should disable id FormControl', () => {
        const formGroup = service.createProblemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
