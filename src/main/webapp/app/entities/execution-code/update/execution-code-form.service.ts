import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IExecutionCode, NewExecutionCode } from '../execution-code.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExecutionCode for edit and NewExecutionCodeFormGroupInput for create.
 */
type ExecutionCodeFormGroupInput = IExecutionCode | PartialWithRequiredKeyOf<NewExecutionCode>;

type ExecutionCodeFormDefaults = Pick<NewExecutionCode, 'id'>;

type ExecutionCodeFormGroupContent = {
  id: FormControl<IExecutionCode['id'] | NewExecutionCode['id']>;
  questionNumber: FormControl<IExecutionCode['questionNumber']>;
  code: FormControl<IExecutionCode['code']>;
  gameCharacter: FormControl<IExecutionCode['gameCharacter']>;
};

export type ExecutionCodeFormGroup = FormGroup<ExecutionCodeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExecutionCodeFormService {
  createExecutionCodeFormGroup(executionCode: ExecutionCodeFormGroupInput = { id: null }): ExecutionCodeFormGroup {
    const executionCodeRawValue = {
      ...this.getFormDefaults(),
      ...executionCode,
    };
    return new FormGroup<ExecutionCodeFormGroupContent>({
      id: new FormControl(
        { value: executionCodeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      questionNumber: new FormControl(executionCodeRawValue.questionNumber, {
        validators: [Validators.required],
      }),
      code: new FormControl(executionCodeRawValue.code, {
        validators: [Validators.required],
      }),
      gameCharacter: new FormControl(executionCodeRawValue.gameCharacter, {
        validators: [Validators.required],
      }),
    });
  }

  getExecutionCode(form: ExecutionCodeFormGroup): IExecutionCode | NewExecutionCode {
    return form.getRawValue() as IExecutionCode | NewExecutionCode;
  }

  resetForm(form: ExecutionCodeFormGroup, executionCode: ExecutionCodeFormGroupInput): void {
    const executionCodeRawValue = { ...this.getFormDefaults(), ...executionCode };
    form.reset(
      {
        ...executionCodeRawValue,
        id: { value: executionCodeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ExecutionCodeFormDefaults {
    return {
      id: null,
    };
  }
}
