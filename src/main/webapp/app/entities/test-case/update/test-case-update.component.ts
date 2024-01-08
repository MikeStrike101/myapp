import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { TestCaseFormService, TestCaseFormGroup } from './test-case-form.service';
import { ITestCase } from '../test-case.model';
import { TestCaseService } from '../service/test-case.service';
import { IProblem } from 'app/entities/problem/problem.model';
import { ProblemService } from 'app/entities/problem/service/problem.service';

@Component({
  selector: 'jhi-test-case-update',
  templateUrl: './test-case-update.component.html',
})
export class TestCaseUpdateComponent implements OnInit {
  isSaving = false;
  testCase: ITestCase | null = null;

  problemsSharedCollection: IProblem[] = [];

  editForm: TestCaseFormGroup = this.testCaseFormService.createTestCaseFormGroup();

  constructor(
    protected testCaseService: TestCaseService,
    protected testCaseFormService: TestCaseFormService,
    protected problemService: ProblemService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProblem = (o1: IProblem | null, o2: IProblem | null): boolean => this.problemService.compareProblem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ testCase }) => {
      this.testCase = testCase;
      if (testCase) {
        this.updateForm(testCase);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const testCase = this.testCaseFormService.getTestCase(this.editForm);
    if (testCase.id !== null) {
      this.subscribeToSaveResponse(this.testCaseService.update(testCase));
    } else {
      this.subscribeToSaveResponse(this.testCaseService.create(testCase));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITestCase>>): void {
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

  protected updateForm(testCase: ITestCase): void {
    this.testCase = testCase;
    this.testCaseFormService.resetForm(this.editForm, testCase);

    this.problemsSharedCollection = this.problemService.addProblemToCollectionIfMissing<IProblem>(
      this.problemsSharedCollection,
      testCase.problem
    );
  }

  protected loadRelationshipsOptions(): void {
    this.problemService
      .query()
      .pipe(map((res: HttpResponse<IProblem[]>) => res.body ?? []))
      .pipe(map((problems: IProblem[]) => this.problemService.addProblemToCollectionIfMissing<IProblem>(problems, this.testCase?.problem)))
      .subscribe((problems: IProblem[]) => (this.problemsSharedCollection = problems));
  }
}
