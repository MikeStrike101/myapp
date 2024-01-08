import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TestCaseComponent } from './list/test-case.component';
import { TestCaseDetailComponent } from './detail/test-case-detail.component';
import { TestCaseUpdateComponent } from './update/test-case-update.component';
import { TestCaseDeleteDialogComponent } from './delete/test-case-delete-dialog.component';
import { TestCaseRoutingModule } from './route/test-case-routing.module';

@NgModule({
  imports: [SharedModule, TestCaseRoutingModule],
  declarations: [TestCaseComponent, TestCaseDetailComponent, TestCaseUpdateComponent, TestCaseDeleteDialogComponent],
})
export class TestCaseModule {}
