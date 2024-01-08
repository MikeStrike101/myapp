import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProblemComponent } from './list/problem.component';
import { ProblemDetailComponent } from './detail/problem-detail.component';
import { ProblemUpdateComponent } from './update/problem-update.component';
import { ProblemDeleteDialogComponent } from './delete/problem-delete-dialog.component';
import { ProblemRoutingModule } from './route/problem-routing.module';

@NgModule({
  imports: [SharedModule, ProblemRoutingModule],
  declarations: [ProblemComponent, ProblemDetailComponent, ProblemUpdateComponent, ProblemDeleteDialogComponent],
})
export class ProblemModule {}
