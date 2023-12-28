import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GameCharacterComponent } from './list/game-character.component';
import { GameCharacterDetailComponent } from './detail/game-character-detail.component';
import { GameCharacterUpdateComponent } from './update/game-character-update.component';
import { GameCharacterDeleteDialogComponent } from './delete/game-character-delete-dialog.component';
import { GameCharacterRoutingModule } from './route/game-character-routing.module';

@NgModule({
  imports: [SharedModule, GameCharacterRoutingModule],
  declarations: [GameCharacterComponent, GameCharacterDetailComponent, GameCharacterUpdateComponent, GameCharacterDeleteDialogComponent],
})
export class GameCharacterModule {}
