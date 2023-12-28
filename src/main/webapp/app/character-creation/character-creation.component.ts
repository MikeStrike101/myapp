import { Component } from '@angular/core';
import { IGameCharacter } from '../entities/game-character/game-character.model';
import { CharacterService } from './character.service';

@Component({
  selector: 'jhi-character-creation',
  templateUrl: './character-creation.component.html',
  styleUrls: ['./character-creation.component.scss'],
})
export class CharacterCreationComponent {
  characterName = '';
  characterEmail = '';
  characterLanguage = '';
  currentColorIndex = 0;
  currentShapeIndex = 0;
  currentAccessoryIndex = 0;
  currentEmojiIndex = 0;

  colors: string[] = ['red', 'blue', 'green'];
  shapes: string[] = ['circle', 'square', 'triangle'];
  accessories: string[] = ['hat', 'glasses', 'necklace'];

  constructor(private characterService: CharacterService) {}

  get currentColor(): string {
    return this.colors[this.currentColorIndex];
  }

  get currentShape(): string {
    return this.shapes[this.currentShapeIndex];
  }

  get currentAccessory(): string {
    return this.accessories[this.currentAccessoryIndex];
  }

  nextColor(): void {
    this.currentColorIndex = (this.currentColorIndex + 1) % this.colors.length;
  }

  previousColor(): void {
    this.currentColorIndex = (this.currentColorIndex - 1 + this.colors.length) % this.colors.length;
  }

  nextShape(): void {
    this.currentShapeIndex = (this.currentShapeIndex + 1) % this.shapes.length;
  }

  previousShape(): void {
    this.currentShapeIndex = (this.currentShapeIndex - 1 + this.shapes.length) % this.shapes.length;
  }

  nextAccessory(): void {
    this.currentAccessoryIndex = (this.currentAccessoryIndex + 1) % this.accessories.length;
  }

  previousAccessory(): void {
    this.currentAccessoryIndex = (this.currentAccessoryIndex - 1 + this.accessories.length) % this.accessories.length;
  }

  createCharacter(): void {
    if (!this.characterName || !this.characterEmail || !this.characterLanguage) {
      alert('Please fill all required fields.');
      return;
    }

    const newGameCharacter: IGameCharacter = {
      id: null,
      name: this.characterName,
      programmingLanguage: this.characterLanguage,
      color: this.currentColor,
      shape: this.currentShape,
      accessory: this.currentAccessory,
      // Add other required fields as per the IGameCharacter interface
    };

    this.characterService.createGameCharacter(newGameCharacter).subscribe({
      next: character => {
        character.experience = 0;
        character.level = 0;
        // console.log('Character created:', character);
        // Handle successful creation (e.g., navigate to a success page or clear the form)
      },
      error: error => {
        alert(error);
        // console.error('There was an error creating the character:', error);
        // Handle errors (e.g., display an error message)
      },
    });
  }
}
