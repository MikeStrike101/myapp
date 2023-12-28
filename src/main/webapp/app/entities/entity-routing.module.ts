import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'game-character',
        data: { pageTitle: 'GameCharacters' },
        loadChildren: () => import('./game-character/game-character.module').then(m => m.GameCharacterModule),
      },
      {
        path: 'progress',
        data: { pageTitle: 'Progresses' },
        loadChildren: () => import('./progress/progress.module').then(m => m.ProgressModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
