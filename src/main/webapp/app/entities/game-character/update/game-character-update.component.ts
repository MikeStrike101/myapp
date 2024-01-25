import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { Router } from '@angular/router';

import { GameCharacterFormService, GameCharacterFormGroup } from './game-character-form.service';
import { IGameCharacter, NewGameCharacter } from '../game-character.model';
import { GameCharacterService } from '../service/game-character.service';
import { IProgress } from 'app/entities/progress/progress.model';
import { ProgressService } from 'app/entities/progress/service/progress.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-game-character-update',
  templateUrl: './game-character-update.component.html',
  styleUrls: ['./game-character-update.component.scss'],
})
export class GameCharacterUpdateComponent implements OnInit {
  isSaving = false;
  gameCharacter: IGameCharacter | null = null;
  showLetsGoMessage = false;
  uniqueLink: string | null = null;
  progressesSharedCollection: IProgress[] = [];
  usersSharedCollection: IUser[] = [];

  programmingLanguages: string[] = ['JavaScript', 'Python', 'Java', 'C#', 'C++', 'PHP', 'TypeScript', 'Ruby', 'Swift', 'Other'];

  shapes: string[] = ['Circle', 'Square', 'Triangle', 'Hexagon'];
  colors: string[] = ['Red', 'Blue', 'Green', 'Yellow'];
  accessories: string[] = ['Hat', 'Glasses', 'Necklace', 'Watch'];

  currentShape: string = this.shapes[0];
  currentColor: string = this.colors[0];
  currentAccessory: string = this.accessories[0];
  editForm!: GameCharacterFormGroup;

  constructor(
    protected gameCharacterService: GameCharacterService,
    protected gameCharacterFormService: GameCharacterFormService,
    protected progressService: ProgressService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router
  ) {}

  compareProgress = (o1: IProgress | null, o2: IProgress | null): boolean => this.progressService.compareProgress(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gameCharacter }) => {
      this.gameCharacter = gameCharacter;
      this.editForm = this.gameCharacterFormService.createGameCharacterFormGroup(gameCharacter);
      if (gameCharacter) {
        this.updateForm(gameCharacter);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  nextShape(): void {
    const currentIndex = this.shapes.indexOf(this.currentShape);
    this.currentShape = this.shapes[(currentIndex + 1) % this.shapes.length];
    this.editForm.controls['shape'].setValue(this.currentShape);
  }

  previousShape(): void {
    const currentIndex = this.shapes.indexOf(this.currentShape);
    this.currentShape = this.shapes[(currentIndex - 1 + this.shapes.length) % this.shapes.length];
    this.editForm.controls['shape'].setValue(this.currentShape);
  }

  nextColor(): void {
    const currentIndex = this.colors.indexOf(this.currentColor);
    this.currentColor = this.colors[(currentIndex + 1) % this.colors.length];
    this.editForm.controls['color'].setValue(this.currentColor);
  }

  previousColor(): void {
    const currentIndex = this.colors.indexOf(this.currentColor);
    this.currentColor = this.colors[(currentIndex - 1 + this.colors.length) % this.colors.length];
    this.editForm.controls['color'].setValue(this.currentColor);
  }

  nextAccessory(): void {
    const currentIndex = this.accessories.indexOf(this.currentAccessory);
    this.currentAccessory = this.accessories[(currentIndex + 1) % this.accessories.length];
    this.editForm.controls['accessory'].setValue(this.currentAccessory);
  }

  previousAccessory(): void {
    const currentIndex = this.accessories.indexOf(this.currentAccessory);
    this.currentAccessory = this.accessories[(currentIndex - 1 + this.accessories.length) % this.accessories.length];
    this.editForm.controls['accessory'].setValue(this.currentAccessory);
  }

  save(): void {
    this.editForm.controls.accessory.setValue(this.currentAccessory);
    this.editForm.controls.shape.setValue(this.currentShape);
    this.editForm.controls.color.setValue(this.currentColor);

    this.isSaving = true;
    const gameCharacter = this.gameCharacterFormService.getGameCharacter(this.editForm);

    if (gameCharacter.id === null) {
      const newGameCharacter: NewGameCharacter = {
        ...gameCharacter,
        id: null,
      };
      this.subscribeToSaveResponse(this.gameCharacterService.create(newGameCharacter));
    } else {
      this.subscribeToSaveResponse(this.gameCharacterService.update(gameCharacter));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGameCharacter>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: response => this.onSaveSuccess(response),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(response: HttpResponse<IGameCharacter>): void {
    const uniqueLink = response.body?.uniqueLink;
    if (uniqueLink) {
      this.uniqueLink = uniqueLink;
      this.showLetsGoMessage = true;
      // Optionally, you can remove or hide the save button here
      // ...
    } else {
      console.error('Unique link is not available in the response');
    }
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(gameCharacter: IGameCharacter): void {
    this.gameCharacter = gameCharacter;
    this.gameCharacterFormService.resetForm(this.editForm, gameCharacter);

    this.progressesSharedCollection = this.progressService.addProgressToCollectionIfMissing<IProgress>(
      this.progressesSharedCollection,
      gameCharacter.progress
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, gameCharacter.user);
  }

  protected loadRelationshipsOptions(): void {
    this.progressService
      .query()
      .pipe(map((res: HttpResponse<IProgress[]>) => res.body ?? []))
      .pipe(
        map((progresses: IProgress[]) =>
          this.progressService.addProgressToCollectionIfMissing<IProgress>(progresses, this.gameCharacter?.progress)
        )
      )
      .subscribe((progresses: IProgress[]) => (this.progressesSharedCollection = progresses));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.gameCharacter?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
