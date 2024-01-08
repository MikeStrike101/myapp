import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITestCase } from '../test-case.model';
import { TestCaseService } from '../service/test-case.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './test-case-delete-dialog.component.html',
})
export class TestCaseDeleteDialogComponent {
  testCase?: ITestCase;

  constructor(protected testCaseService: TestCaseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.testCaseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
