import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserProblemComponent } from '../list/user-problem.component';
import { UserProblemDetailComponent } from '../detail/user-problem-detail.component';
import { UserProblemUpdateComponent } from '../update/user-problem-update.component';
import { UserProblemRoutingResolveService } from './user-problem-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const userProblemRoute: Routes = [
  {
    path: '',
    component: UserProblemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserProblemDetailComponent,
    resolve: {
      userProblem: UserProblemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserProblemUpdateComponent,
    resolve: {
      userProblem: UserProblemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserProblemUpdateComponent,
    resolve: {
      userProblem: UserProblemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(userProblemRoute)],
  exports: [RouterModule],
})
export class UserProblemRoutingModule {}
