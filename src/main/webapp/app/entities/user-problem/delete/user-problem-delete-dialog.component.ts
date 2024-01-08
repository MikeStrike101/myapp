import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserProblem } from '../user-problem.model';
import { UserProblemService } from '../service/user-problem.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './user-problem-delete-dialog.component.html',
})
export class UserProblemDeleteDialogComponent {
  userProblem?: IUserProblem;

  constructor(protected userProblemService: UserProblemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userProblemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
