import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExecutionCodeComponent } from '../list/execution-code.component';
import { ExecutionCodeDetailComponent } from '../detail/execution-code-detail.component';
import { ExecutionCodeUpdateComponent } from '../update/execution-code-update.component';
import { ExecutionCodeRoutingResolveService } from './execution-code-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const executionCodeRoute: Routes = [
  {
    path: '',
    component: ExecutionCodeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExecutionCodeDetailComponent,
    resolve: {
      executionCode: ExecutionCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExecutionCodeUpdateComponent,
    resolve: {
      executionCode: ExecutionCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExecutionCodeUpdateComponent,
    resolve: {
      executionCode: ExecutionCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(executionCodeRoute)],
  exports: [RouterModule],
})
export class ExecutionCodeRoutingModule {}
