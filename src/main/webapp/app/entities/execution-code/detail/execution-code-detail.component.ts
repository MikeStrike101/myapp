import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExecutionCode } from '../execution-code.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-execution-code-detail',
  templateUrl: './execution-code-detail.component.html',
})
export class ExecutionCodeDetailComponent implements OnInit {
  executionCode: IExecutionCode | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ executionCode }) => {
      this.executionCode = executionCode;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
