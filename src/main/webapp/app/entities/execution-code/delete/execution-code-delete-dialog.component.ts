import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExecutionCode } from '../execution-code.model';
import { ExecutionCodeService } from '../service/execution-code.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './execution-code-delete-dialog.component.html',
})
export class ExecutionCodeDeleteDialogComponent {
  executionCode?: IExecutionCode;

  constructor(protected executionCodeService: ExecutionCodeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.executionCodeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
