import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITestCase, NewTestCase } from '../test-case.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITestCase for edit and NewTestCaseFormGroupInput for create.
 */
type TestCaseFormGroupInput = ITestCase | PartialWithRequiredKeyOf<NewTestCase>;

type TestCaseFormDefaults = Pick<NewTestCase, 'id'>;

type TestCaseFormGroupContent = {
  id: FormControl<ITestCase['id'] | NewTestCase['id']>;
  input: FormControl<ITestCase['input']>;
  output: FormControl<ITestCase['output']>;
  problem: FormControl<ITestCase['problem']>;
};

export type TestCaseFormGroup = FormGroup<TestCaseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TestCaseFormService {
  createTestCaseFormGroup(testCase: TestCaseFormGroupInput = { id: null }): TestCaseFormGroup {
    const testCaseRawValue = {
      ...this.getFormDefaults(),
      ...testCase,
    };
    return new FormGroup<TestCaseFormGroupContent>({
      id: new FormControl(
        { value: testCaseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      input: new FormControl(testCaseRawValue.input, {
        validators: [Validators.required],
      }),
      output: new FormControl(testCaseRawValue.output, {
        validators: [Validators.required],
      }),
      problem: new FormControl(testCaseRawValue.problem),
    });
  }

  getTestCase(form: TestCaseFormGroup): ITestCase | NewTestCase {
    return form.getRawValue() as ITestCase | NewTestCase;
  }

  resetForm(form: TestCaseFormGroup, testCase: TestCaseFormGroupInput): void {
    const testCaseRawValue = { ...this.getFormDefaults(), ...testCase };
    form.reset(
      {
        ...testCaseRawValue,
        id: { value: testCaseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TestCaseFormDefaults {
    return {
      id: null,
    };
  }
}
