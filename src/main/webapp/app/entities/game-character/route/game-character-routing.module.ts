import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GameCharacterComponent } from '../list/game-character.component';
import { GameCharacterDetailComponent } from '../detail/game-character-detail.component';
import { GameCharacterUpdateComponent } from '../update/game-character-update.component';
import { GameCharacterRoutingResolveService } from './game-character-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const gameCharacterRoute: Routes = [
  {
    path: '',
    component: GameCharacterComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GameCharacterDetailComponent,
    resolve: {
      gameCharacter: GameCharacterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GameCharacterUpdateComponent,
    resolve: {
      gameCharacter: GameCharacterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GameCharacterUpdateComponent,
    resolve: {
      gameCharacter: GameCharacterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gameCharacterRoute)],
  exports: [RouterModule],
})
export class GameCharacterRoutingModule {}
