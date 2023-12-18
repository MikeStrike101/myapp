import { Component, OnInit } from '@angular/core';
import { CharacterService } from './character.service';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-character-creation',
  templateUrl: './character-creation.component.html',
  styleUrls: ['./character-creation.component.scss'],
})
export class CharacterCreationComponent implements OnInit {
  characterName = '';
  characterFeatures: any = {};
  emojis: string[] = ['😀', '😃', '😄', '😁', '😆', '😅', '😂'];
  currentEmojiIndex = 0;

  constructor(private characterService: CharacterService, private router: Router) {}

  ngOnInit(): void {
    this.loadInitialCharacterFeatures();
  }

  createCharacter(): void {
    this.characterService.createCharacter(this.characterName, this.characterFeatures).subscribe(
      result => {
        this.router.navigate(['/character-profile']);
      },
      error => {
        console.error('Error creating character', error);
      }
    );
  }

  nextEmoji(): void {
    this.currentEmojiIndex = (this.currentEmojiIndex + 1) % this.emojis.length;
  }

  previousEmoji(): void {
    this.currentEmojiIndex = (this.currentEmojiIndex - 1 + this.emojis.length) % this.emojis.length;
  }

  private loadInitialCharacterFeatures(): void {
    this.characterFeatures = {
      strength: 10,
      intelligence: 10,
      charisma: 10,
    };
  }
}
