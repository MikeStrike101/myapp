import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUserProblem, NewUserProblem } from '../user-problem.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserProblem for edit and NewUserProblemFormGroupInput for create.
 */
type UserProblemFormGroupInput = IUserProblem | PartialWithRequiredKeyOf<NewUserProblem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUserProblem | NewUserProblem> = Omit<T, 'solvedAt'> & {
  solvedAt?: string | null;
};

type UserProblemFormRawValue = FormValueOf<IUserProblem>;

type NewUserProblemFormRawValue = FormValueOf<NewUserProblem>;

type UserProblemFormDefaults = Pick<NewUserProblem, 'id' | 'solvedAt'>;

type UserProblemFormGroupContent = {
  id: FormControl<UserProblemFormRawValue['id'] | NewUserProblem['id']>;
  solvedAt: FormControl<UserProblemFormRawValue['solvedAt']>;
  passedTestCases: FormControl<UserProblemFormRawValue['passedTestCases']>;
  xpAwarded: FormControl<UserProblemFormRawValue['xpAwarded']>;
  user: FormControl<UserProblemFormRawValue['user']>;
  problem: FormControl<UserProblemFormRawValue['problem']>;
};

export type UserProblemFormGroup = FormGroup<UserProblemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserProblemFormService {
  createUserProblemFormGroup(userProblem: UserProblemFormGroupInput = { id: null }): UserProblemFormGroup {
    const userProblemRawValue = this.convertUserProblemToUserProblemRawValue({
      ...this.getFormDefaults(),
      ...userProblem,
    });
    return new FormGroup<UserProblemFormGroupContent>({
      id: new FormControl(
        { value: userProblemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      solvedAt: new FormControl(userProblemRawValue.solvedAt),
      passedTestCases: new FormControl(userProblemRawValue.passedTestCases, {
        validators: [Validators.required, Validators.min(0)],
      }),
      xpAwarded: new FormControl(userProblemRawValue.xpAwarded, {
        validators: [Validators.required, Validators.min(0)],
      }),
      user: new FormControl(userProblemRawValue.user),
      problem: new FormControl(userProblemRawValue.problem),
    });
  }

  getUserProblem(form: UserProblemFormGroup): IUserProblem | NewUserProblem {
    return this.convertUserProblemRawValueToUserProblem(form.getRawValue() as UserProblemFormRawValue | NewUserProblemFormRawValue);
  }

  resetForm(form: UserProblemFormGroup, userProblem: UserProblemFormGroupInput): void {
    const userProblemRawValue = this.convertUserProblemToUserProblemRawValue({ ...this.getFormDefaults(), ...userProblem });
    form.reset(
      {
        ...userProblemRawValue,
        id: { value: userProblemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UserProblemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      solvedAt: currentTime,
    };
  }

  private convertUserProblemRawValueToUserProblem(
    rawUserProblem: UserProblemFormRawValue | NewUserProblemFormRawValue
  ): IUserProblem | NewUserProblem {
    return {
      ...rawUserProblem,
      solvedAt: dayjs(rawUserProblem.solvedAt, DATE_TIME_FORMAT),
    };
  }

  private convertUserProblemToUserProblemRawValue(
    userProblem: IUserProblem | (Partial<NewUserProblem> & UserProblemFormDefaults)
  ): UserProblemFormRawValue | PartialWithRequiredKeyOf<NewUserProblemFormRawValue> {
    return {
      ...userProblem,
      solvedAt: userProblem.solvedAt ? userProblem.solvedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
