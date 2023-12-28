import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GameCharacterFormService } from './game-character-form.service';
import { GameCharacterService } from '../service/game-character.service';
import { IGameCharacter } from '../game-character.model';
import { IProgress } from 'app/entities/progress/progress.model';
import { ProgressService } from 'app/entities/progress/service/progress.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { GameCharacterUpdateComponent } from './game-character-update.component';

describe('GameCharacter Management Update Component', () => {
  let comp: GameCharacterUpdateComponent;
  let fixture: ComponentFixture<GameCharacterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gameCharacterFormService: GameCharacterFormService;
  let gameCharacterService: GameCharacterService;
  let progressService: ProgressService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GameCharacterUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(GameCharacterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GameCharacterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gameCharacterFormService = TestBed.inject(GameCharacterFormService);
    gameCharacterService = TestBed.inject(GameCharacterService);
    progressService = TestBed.inject(ProgressService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Progress query and add missing value', () => {
      const gameCharacter: IGameCharacter = { id: 456 };
      const progress: IProgress = { id: 14007 };
      gameCharacter.progress = progress;

      const progressCollection: IProgress[] = [{ id: 13864 }];
      jest.spyOn(progressService, 'query').mockReturnValue(of(new HttpResponse({ body: progressCollection })));
      const additionalProgresses = [progress];
      const expectedCollection: IProgress[] = [...additionalProgresses, ...progressCollection];
      jest.spyOn(progressService, 'addProgressToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gameCharacter });
      comp.ngOnInit();

      expect(progressService.query).toHaveBeenCalled();
      expect(progressService.addProgressToCollectionIfMissing).toHaveBeenCalledWith(
        progressCollection,
        ...additionalProgresses.map(expect.objectContaining)
      );
      expect(comp.progressesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const gameCharacter: IGameCharacter = { id: 456 };
      const user: IUser = { id: '269f5559-6ce1-4bce-bfce-9a73cf42f4ac' };
      gameCharacter.user = user;

      const userCollection: IUser[] = [{ id: 'a1380b4d-254d-4e5f-af1b-1ce3a7e5c4a0' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ gameCharacter });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const gameCharacter: IGameCharacter = { id: 456 };
      const progress: IProgress = { id: 88371 };
      gameCharacter.progress = progress;
      const user: IUser = { id: 'fc46f1ca-0685-4a64-9905-42135dd40bdf' };
      gameCharacter.user = user;

      activatedRoute.data = of({ gameCharacter });
      comp.ngOnInit();

      expect(comp.progressesSharedCollection).toContain(progress);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.gameCharacter).toEqual(gameCharacter);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameCharacter>>();
      const gameCharacter = { id: 123 };
      jest.spyOn(gameCharacterFormService, 'getGameCharacter').mockReturnValue(gameCharacter);
      jest.spyOn(gameCharacterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameCharacter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameCharacter }));
      saveSubject.complete();

      // THEN
      expect(gameCharacterFormService.getGameCharacter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gameCharacterService.update).toHaveBeenCalledWith(expect.objectContaining(gameCharacter));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameCharacter>>();
      const gameCharacter = { id: 123 };
      jest.spyOn(gameCharacterFormService, 'getGameCharacter').mockReturnValue({ id: null });
      jest.spyOn(gameCharacterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameCharacter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gameCharacter }));
      saveSubject.complete();

      // THEN
      expect(gameCharacterFormService.getGameCharacter).toHaveBeenCalled();
      expect(gameCharacterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameCharacter>>();
      const gameCharacter = { id: 123 };
      jest.spyOn(gameCharacterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameCharacter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gameCharacterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProgress', () => {
      it('Should forward to progressService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(progressService, 'compareProgress');
        comp.compareProgress(entity, entity2);
        expect(progressService.compareProgress).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
