import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GameCharacterFormService } from './game-character-form.service';
import { GameCharacterService } from '../service/game-character.service';
import { IGameCharacter } from '../game-character.model';

import { GameCharacterUpdateComponent } from './game-character-update.component';

describe('GameCharacter Management Update Component', () => {
  let comp: GameCharacterUpdateComponent;
  let fixture: ComponentFixture<GameCharacterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gameCharacterFormService: GameCharacterFormService;
  let gameCharacterService: GameCharacterService;

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

    comp = fixture.componentInstance;
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

      // Log the initial state
      console.log('Initial game character data:', gameCharacter);
      console.log('Initial form data:', comp.editForm.getRawValue());

      // WHEN
      comp.save();
      saveSubject.next(new HttpResponse({ body: gameCharacter }));
      saveSubject.complete();

      // Log the result
      console.log('Save operation completed. Is saving:', comp.isSaving);

      // THEN
      expect(gameCharacterFormService.getGameCharacter).toHaveBeenCalled();
      expect(gameCharacterService.update).toHaveBeenCalledWith(expect.objectContaining(gameCharacter));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', fakeAsync(() => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGameCharacter>>();
      const gameCharacter = { id: 123 };
      const mockUniqueLink = 'mock-unique-link';
      jest.spyOn(gameCharacterFormService, 'getGameCharacter').mockReturnValue({ id: null });
      jest.spyOn(gameCharacterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(gameCharacterFormService, 'generateUniqueLink').mockReturnValue(of(mockUniqueLink));
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gameCharacter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      tick();
      saveSubject.next(new HttpResponse({ body: gameCharacter }));
      saveSubject.complete();
      tick();

      console.log('Save operation completed. Is saving:', comp.isSaving);

      // THEN
      expect(gameCharacterFormService.getGameCharacter).toHaveBeenCalled();
      expect(gameCharacterService.create).toHaveBeenCalledWith(
        expect.objectContaining({
          id: null,
          uniqueLink: mockUniqueLink,
        })
      );
      expect(comp.isSaving).toEqual(false);
    }));

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
      saveSubject.error('This is an error!');

      // Log the result
      console.log('Save operation completed with error. Is saving:', comp.isSaving);

      // THEN
      expect(gameCharacterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
