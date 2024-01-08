import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProblem, NewProblem } from '../problem.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProblem for edit and NewProblemFormGroupInput for create.
 */
type ProblemFormGroupInput = IProblem | PartialWithRequiredKeyOf<NewProblem>;

type ProblemFormDefaults = Pick<NewProblem, 'id'>;

type ProblemFormGroupContent = {
  id: FormControl<IProblem['id'] | NewProblem['id']>;
  title: FormControl<IProblem['title']>;
  description: FormControl<IProblem['description']>;
  level: FormControl<IProblem['level']>;
  xpReward: FormControl<IProblem['xpReward']>;
};

export type ProblemFormGroup = FormGroup<ProblemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProblemFormService {
  createProblemFormGroup(problem: ProblemFormGroupInput = { id: null }): ProblemFormGroup {
    const problemRawValue = {
      ...this.getFormDefaults(),
      ...problem,
    };
    return new FormGroup<ProblemFormGroupContent>({
      id: new FormControl(
        { value: problemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(problemRawValue.title, {
        validators: [Validators.required, Validators.minLength(5), Validators.maxLength(150)],
      }),
      description: new FormControl(problemRawValue.description, {
        validators: [Validators.required, Validators.minLength(20)],
      }),
      level: new FormControl(problemRawValue.level, {
        validators: [Validators.required, Validators.min(1)],
      }),
      xpReward: new FormControl(problemRawValue.xpReward, {
        validators: [Validators.required, Validators.min(0)],
      }),
    });
  }

  getProblem(form: ProblemFormGroup): IProblem | NewProblem {
    return form.getRawValue() as IProblem | NewProblem;
  }

  resetForm(form: ProblemFormGroup, problem: ProblemFormGroupInput): void {
    const problemRawValue = { ...this.getFormDefaults(), ...problem };
    form.reset(
      {
        ...problemRawValue,
        id: { value: problemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProblemFormDefaults {
    return {
      id: null,
    };
  }
}
