import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ProblemFormService, ProblemFormGroup } from './problem-form.service';
import { IProblem } from '../problem.model';
import { ProblemService } from '../service/problem.service';

@Component({
  selector: 'jhi-problem-update',
  templateUrl: './problem-update.component.html',
})
export class ProblemUpdateComponent implements OnInit {
  isSaving = false;
  problem: IProblem | null = null;

  editForm: ProblemFormGroup = this.problemFormService.createProblemFormGroup();

  constructor(
    protected problemService: ProblemService,
    protected problemFormService: ProblemFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ problem }) => {
      this.problem = problem;
      if (problem) {
        this.updateForm(problem);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const problem = this.problemFormService.getProblem(this.editForm);
    if (problem.id !== null) {
      this.subscribeToSaveResponse(this.problemService.update(problem));
    } else {
      this.subscribeToSaveResponse(this.problemService.create(problem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProblem>>): void {
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

  protected updateForm(problem: IProblem): void {
    this.problem = problem;
    this.problemFormService.resetForm(this.editForm, problem);
  }
}
