import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { UserProblemFormService, UserProblemFormGroup } from './user-problem-form.service';
import { IUserProblem } from '../user-problem.model';
import { UserProblemService } from '../service/user-problem.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProblem } from 'app/entities/problem/problem.model';
import { ProblemService } from 'app/entities/problem/service/problem.service';

@Component({
  selector: 'jhi-user-problem-update',
  templateUrl: './user-problem-update.component.html',
})
export class UserProblemUpdateComponent implements OnInit {
  isSaving = false;
  userProblem: IUserProblem | null = null;

  usersSharedCollection: IUser[] = [];
  problemsSharedCollection: IProblem[] = [];

  editForm: UserProblemFormGroup = this.userProblemFormService.createUserProblemFormGroup();

  constructor(
    protected userProblemService: UserProblemService,
    protected userProblemFormService: UserProblemFormService,
    protected userService: UserService,
    protected problemService: ProblemService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareProblem = (o1: IProblem | null, o2: IProblem | null): boolean => this.problemService.compareProblem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userProblem }) => {
      this.userProblem = userProblem;
      if (userProblem) {
        this.updateForm(userProblem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userProblem = this.userProblemFormService.getUserProblem(this.editForm);
    if (userProblem.id !== null) {
      this.subscribeToSaveResponse(this.userProblemService.update(userProblem));
    } else {
      this.subscribeToSaveResponse(this.userProblemService.create(userProblem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserProblem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userProblem: IUserProblem): void {
    this.userProblem = userProblem;
    this.userProblemFormService.resetForm(this.editForm, userProblem);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, userProblem.user);
    this.problemsSharedCollection = this.problemService.addProblemToCollectionIfMissing<IProblem>(
      this.problemsSharedCollection,
      userProblem.problem
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.userProblem?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.problemService
      .query()
      .pipe(map((res: HttpResponse<IProblem[]>) => res.body ?? []))
      .pipe(
        map((problems: IProblem[]) => this.problemService.addProblemToCollectionIfMissing<IProblem>(problems, this.userProblem?.problem))
      )
      .subscribe((problems: IProblem[]) => (this.problemsSharedCollection = problems));
  }
}
