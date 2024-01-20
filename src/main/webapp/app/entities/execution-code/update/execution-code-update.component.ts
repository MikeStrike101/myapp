import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ExecutionCodeFormService, ExecutionCodeFormGroup } from './execution-code-form.service';
import { IExecutionCode } from '../execution-code.model';
import { ExecutionCodeService } from '../service/execution-code.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-execution-code-update',
  templateUrl: './execution-code-update.component.html',
})
export class ExecutionCodeUpdateComponent implements OnInit {
  isSaving = false;
  executionCode: IExecutionCode | null = null;

  editForm: ExecutionCodeFormGroup = this.executionCodeFormService.createExecutionCodeFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected executionCodeService: ExecutionCodeService,
    protected executionCodeFormService: ExecutionCodeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ executionCode }) => {
      this.executionCode = executionCode;
      if (executionCode) {
        this.updateForm(executionCode);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('myappApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const executionCode = this.executionCodeFormService.getExecutionCode(this.editForm);
    if (executionCode.id !== null) {
      this.subscribeToSaveResponse(this.executionCodeService.update(executionCode));
    } else {
      this.subscribeToSaveResponse(this.executionCodeService.create(executionCode));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExecutionCode>>): void {
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

  protected updateForm(executionCode: IExecutionCode): void {
    this.executionCode = executionCode;
    this.executionCodeFormService.resetForm(this.editForm, executionCode);
  }
}
