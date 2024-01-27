import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { IGameCharacter, NewGameCharacter } from '../game-character.model';
import { catchError, map, switchMap } from 'rxjs/operators';
import { BehaviorSubject, Observable, combineLatest, of } from 'rxjs';
import { adjectives } from './adjectives';
import { nouns } from './nouns';
/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGameCharacter for edit and NewGameCharacterFormGroupInput for create.
 */
type GameCharacterFormGroupInput = IGameCharacter | PartialWithRequiredKeyOf<NewGameCharacter>;

type GameCharacterFormDefaults = Pick<NewGameCharacter, 'id' | 'level' | 'experience' | 'progress' | 'uniqueLink'>;

type GameCharacterFormGroupContent = {
  id: FormControl<IGameCharacter['id'] | NewGameCharacter['id']>;
  name: FormControl<IGameCharacter['name']>;
  email: FormControl<IGameCharacter['email']>;
  level: FormControl<IGameCharacter['level']>;
  experience: FormControl<IGameCharacter['experience']>;
  shape: FormControl<IGameCharacter['shape']>;
  color: FormControl<IGameCharacter['color']>;
  accessory: FormControl<IGameCharacter['accessory']>;
  programmingLanguage: FormControl<IGameCharacter['programmingLanguage']>;
  uniqueLink: FormControl<IGameCharacter['uniqueLink']>;
  progress: FormControl<IGameCharacter['progress']>;
  user: FormControl<IGameCharacter['user']>;
};

export type GameCharacterFormGroup = FormGroup<GameCharacterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GameCharacterFormService {
  private adjectives: string[] = adjectives;
  private nouns: string[] = nouns;

  constructor(private http: HttpClient) {
    this.loadWords(); // Load words as soon as the service is created
  }

  private loadWords(): void {
    this.adjectives = adjectives;
    this.nouns = nouns;
  }

  public generateUniqueLink(): Observable<string> {
    const adjective = this.adjectives[Math.floor(Math.random() * this.adjectives.length)];
    const noun = this.nouns[Math.floor(Math.random() * this.nouns.length)];
    const randomNumber = Math.floor(1000 + Math.random() * 9000);
    const potentialLink = `${adjective}-${noun}-${randomNumber}`.toLowerCase();

    // Check the uniqueness of the link by making an HTTP GET request to the backend
    return this.http.get<boolean>(`/api/game-characters/unique-link/${potentialLink}`).pipe(
      switchMap(isUnique => {
        if (isUnique) {
          // If the link is unique, return it
          return of(potentialLink);
        } else {
          // If the link is not unique, recursively call generateUniqueLink until a unique one is found
          return this.generateUniqueLink();
        }
      }),
      catchError(error => {
        // Handle error appropriately
        console.error('Error checking link uniqueness', error);
        return of('Error generating link');
      })
    );
  }

  createGameCharacterFormGroup(gameCharacter: GameCharacterFormGroupInput = { id: null }): GameCharacterFormGroup {
    const gameCharacterRawValue = {
      ...this.getFormDefaults(),
      ...gameCharacter,
    };
    return new FormGroup<GameCharacterFormGroupContent>({
      id: new FormControl(
        { value: gameCharacterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(gameCharacterRawValue.name, {
        validators: [Validators.required, Validators.minLength(2)],
      }),
      email: new FormControl(gameCharacterRawValue.email, {
        validators: [Validators.required],
      }),
      level: new FormControl(
        { value: gameCharacterRawValue.level, disabled: true },
        {
          validators: [Validators.required, Validators.min(0)],
        }
      ),
      experience: new FormControl(
        { value: gameCharacterRawValue.experience, disabled: true },
        {
          validators: [Validators.required, Validators.min(0)],
        }
      ),
      shape: new FormControl(gameCharacterRawValue.shape, {
        validators: [Validators.required],
      }),
      color: new FormControl(gameCharacterRawValue.color, {
        validators: [Validators.required],
      }),
      accessory: new FormControl(gameCharacterRawValue.accessory),
      programmingLanguage: new FormControl(gameCharacterRawValue.programmingLanguage, {
        validators: [Validators.required],
      }),
      uniqueLink: new FormControl(
        { value: gameCharacterRawValue.uniqueLink, disabled: true },
        {
          validators: [Validators.required],
        }
      ),
      progress: new FormControl({ value: gameCharacterRawValue.progress, disabled: true }),
      user: new FormControl(gameCharacterRawValue.user),
    });
  }

  getGameCharacter(form: GameCharacterFormGroup): IGameCharacter | NewGameCharacter {
    return form.getRawValue() as IGameCharacter | NewGameCharacter;
  }

  resetForm(form: GameCharacterFormGroup, gameCharacter: GameCharacterFormGroupInput): void {
    const gameCharacterRawValue = { ...this.getFormDefaults(), ...gameCharacter };
    form.reset(
      {
        ...gameCharacterRawValue,
        id: { value: gameCharacterRawValue.id, disabled: true },
        experience: { value: gameCharacterRawValue.experience, disabled: true },
        level: { value: gameCharacterRawValue.level, disabled: true },
        progress: { value: gameCharacterRawValue.progress, disabled: true },
        uniqueLink: { value: this.generateUniqueLink(), disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private generateRandomString(length: number): string {
    const possibleChars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let randomString = '';
    for (let i = 0; i < length; i++) {
      randomString += possibleChars.charAt(Math.floor(Math.random() * possibleChars.length));
    }
    return randomString;
  }

  private getFormDefaults(): GameCharacterFormDefaults {
    return {
      id: null,
      level: 0,
      experience: 0,
      progress: null,
      uniqueLink: '',
    };
  }
}
