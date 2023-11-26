import { Component, OnInit } from '@angular/core';
import { CharacterService } from './character.service';

@Component({
  selector: 'jhi-character-creation',
  templateUrl: './character-creation.component.html',
  styleUrls: ['./character-creation.component.scss'],
})
export class CharacterCreationComponent implements OnInit {
  characterName = '';
  characterFeatures: any = {};

  constructor(private characterService: CharacterService) {}

  ngOnInit(): void {
    this.loadInitialCharacterFeatures();
  }

  createCharacter(): void {
    this.characterService.createCharacter(this.characterName, this.characterFeatures).subscribe(
      result => {
        console.log('Character created successfully', result);
      },
      error => {
        console.error('Error creating character', error);
      }
    );
  }

  private loadInitialCharacterFeatures(): void {
    this.characterFeatures = {
      strength: 10,
      intelligence: 10,
      charisma: 10,
      // Other features will follow
    };
  }
}
