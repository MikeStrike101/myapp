import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProblemComponent } from '../list/problem.component';
import { ProblemDetailComponent } from '../detail/problem-detail.component';
import { ProblemUpdateComponent } from '../update/problem-update.component';
import { ProblemRoutingResolveService } from './problem-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const problemRoute: Routes = [
  {
    path: '',
    component: ProblemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProblemDetailComponent,
    resolve: {
      problem: ProblemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProblemUpdateComponent,
    resolve: {
      problem: ProblemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProblemUpdateComponent,
    resolve: {
      problem: ProblemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(problemRoute)],
  exports: [RouterModule],
})
export class ProblemRoutingModule {}
