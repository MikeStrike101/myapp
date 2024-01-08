import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TestCaseComponent } from '../list/test-case.component';
import { TestCaseDetailComponent } from '../detail/test-case-detail.component';
import { TestCaseUpdateComponent } from '../update/test-case-update.component';
import { TestCaseRoutingResolveService } from './test-case-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const testCaseRoute: Routes = [
  {
    path: '',
    component: TestCaseComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TestCaseDetailComponent,
    resolve: {
      testCase: TestCaseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TestCaseUpdateComponent,
    resolve: {
      testCase: TestCaseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TestCaseUpdateComponent,
    resolve: {
      testCase: TestCaseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(testCaseRoute)],
  exports: [RouterModule],
})
export class TestCaseRoutingModule {}
