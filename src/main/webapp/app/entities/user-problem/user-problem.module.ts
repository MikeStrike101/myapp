import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UserProblemComponent } from './list/user-problem.component';
import { UserProblemDetailComponent } from './detail/user-problem-detail.component';
import { UserProblemUpdateComponent } from './update/user-problem-update.component';
import { UserProblemDeleteDialogComponent } from './delete/user-problem-delete-dialog.component';
import { UserProblemRoutingModule } from './route/user-problem-routing.module';

@NgModule({
  imports: [SharedModule, UserProblemRoutingModule],
  declarations: [UserProblemComponent, UserProblemDetailComponent, UserProblemUpdateComponent, UserProblemDeleteDialogComponent],
})
export class UserProblemModule {}
