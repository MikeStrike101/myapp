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
      {
        path: 'problem',
        data: { pageTitle: 'Problems' },
        loadChildren: () => import('./problem/problem.module').then(m => m.ProblemModule),
      },
      {
        path: 'test-case',
        data: { pageTitle: 'TestCases' },
        loadChildren: () => import('./test-case/test-case.module').then(m => m.TestCaseModule),
      },
      {
        path: 'user-problem',
        data: { pageTitle: 'UserProblems' },
        loadChildren: () => import('./user-problem/user-problem.module').then(m => m.UserProblemModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
