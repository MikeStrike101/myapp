import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGameCharacter } from '../game-character.model';

@Component({
  selector: 'jhi-game-character-detail',
  templateUrl: './game-character-detail.component.html',
  styleUrls: ['./game-character-detail.component.scss'],
})
export class GameCharacterDetailComponent implements OnInit {
  gameCharacter: IGameCharacter | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gameCharacter }) => {
      this.gameCharacter = gameCharacter;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
