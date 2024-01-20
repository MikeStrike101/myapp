import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../execution-code.test-samples';

import { ExecutionCodeFormService } from './execution-code-form.service';

describe('ExecutionCode Form Service', () => {
  let service: ExecutionCodeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExecutionCodeFormService);
  });

  describe('Service methods', () => {
    describe('createExecutionCodeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExecutionCodeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            questionNumber: expect.any(Object),
            code: expect.any(Object),
            gameCharacter: expect.any(Object),
          })
        );
      });

      it('passing IExecutionCode should create a new form with FormGroup', () => {
        const formGroup = service.createExecutionCodeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            questionNumber: expect.any(Object),
            code: expect.any(Object),
            gameCharacter: expect.any(Object),
          })
        );
      });
    });

    describe('getExecutionCode', () => {
      it('should return NewExecutionCode for default ExecutionCode initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createExecutionCodeFormGroup(sampleWithNewData);

        const executionCode = service.getExecutionCode(formGroup) as any;

        expect(executionCode).toMatchObject(sampleWithNewData);
      });

      it('should return NewExecutionCode for empty ExecutionCode initial value', () => {
        const formGroup = service.createExecutionCodeFormGroup();

        const executionCode = service.getExecutionCode(formGroup) as any;

        expect(executionCode).toMatchObject({});
      });

      it('should return IExecutionCode', () => {
        const formGroup = service.createExecutionCodeFormGroup(sampleWithRequiredData);

        const executionCode = service.getExecutionCode(formGroup) as any;

        expect(executionCode).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExecutionCode should not enable id FormControl', () => {
        const formGroup = service.createExecutionCodeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExecutionCode should disable id FormControl', () => {
        const formGroup = service.createExecutionCodeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
