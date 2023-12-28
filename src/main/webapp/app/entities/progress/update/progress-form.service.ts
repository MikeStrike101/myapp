import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProgress, NewProgress } from '../progress.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProgress for edit and NewProgressFormGroupInput for create.
 */
type ProgressFormGroupInput = IProgress | PartialWithRequiredKeyOf<NewProgress>;

type ProgressFormDefaults = Pick<NewProgress, 'id'>;

type ProgressFormGroupContent = {
  id: FormControl<IProgress['id'] | NewProgress['id']>;
  status: FormControl<IProgress['status']>;
  currentLesson: FormControl<IProgress['currentLesson']>;
  xp: FormControl<IProgress['xp']>;
};

export type ProgressFormGroup = FormGroup<ProgressFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProgressFormService {
  createProgressFormGroup(progress: ProgressFormGroupInput = { id: null }): ProgressFormGroup {
    const progressRawValue = {
      ...this.getFormDefaults(),
      ...progress,
    };
    return new FormGroup<ProgressFormGroupContent>({
      id: new FormControl(
        { value: progressRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      status: new FormControl(progressRawValue.status, {
        validators: [Validators.required],
      }),
      currentLesson: new FormControl(progressRawValue.currentLesson),
      xp: new FormControl(progressRawValue.xp, {
        validators: [Validators.required, Validators.min(0)],
      }),
    });
  }

  getProgress(form: ProgressFormGroup): IProgress | NewProgress {
    return form.getRawValue() as IProgress | NewProgress;
  }

  resetForm(form: ProgressFormGroup, progress: ProgressFormGroupInput): void {
    const progressRawValue = { ...this.getFormDefaults(), ...progress };
    form.reset(
      {
        ...progressRawValue,
        id: { value: progressRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProgressFormDefaults {
    return {
      id: null,
    };
  }
}
