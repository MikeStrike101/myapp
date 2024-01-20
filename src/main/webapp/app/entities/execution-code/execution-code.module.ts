import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExecutionCodeComponent } from './list/execution-code.component';
import { ExecutionCodeDetailComponent } from './detail/execution-code-detail.component';
import { ExecutionCodeUpdateComponent } from './update/execution-code-update.component';
import { ExecutionCodeDeleteDialogComponent } from './delete/execution-code-delete-dialog.component';
import { ExecutionCodeRoutingModule } from './route/execution-code-routing.module';

@NgModule({
  imports: [SharedModule, ExecutionCodeRoutingModule],
  declarations: [ExecutionCodeComponent, ExecutionCodeDetailComponent, ExecutionCodeUpdateComponent, ExecutionCodeDeleteDialogComponent],
})
export class ExecutionCodeModule {}
