import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-character-creation',
  templateUrl: './character-creation.component.html',
  styleUrls: ['./character-creation.component.scss'],
})
export class CharacterCreationComponent implements OnInit {
  characterName: string = '';
  characterFeatures: any = {};

  constructor() {}

  ngOnInit(): void {}

  createCharacter(): void {}
}
