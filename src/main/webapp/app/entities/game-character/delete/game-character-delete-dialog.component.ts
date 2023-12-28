import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGameCharacter } from '../game-character.model';
import { GameCharacterService } from '../service/game-character.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './game-character-delete-dialog.component.html',
})
export class GameCharacterDeleteDialogComponent {
  gameCharacter?: IGameCharacter;

  constructor(protected gameCharacterService: GameCharacterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gameCharacterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
