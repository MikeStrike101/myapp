import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../game-character.test-samples';

import { GameCharacterFormService } from './game-character-form.service';

describe('GameCharacter Form Service', () => {
  let service: GameCharacterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GameCharacterFormService);
  });

  describe('Service methods', () => {
    describe('createGameCharacterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGameCharacterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            email: expect.any(Object),
            level: expect.any(Object),
            experience: expect.any(Object),
            shape: expect.any(Object),
            color: expect.any(Object),
            accessory: expect.any(Object),
            programmingLanguage: expect.any(Object),
            uniqueLink: expect.any(Object),
            progress: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });

      it('passing IGameCharacter should create a new form with FormGroup', () => {
        const formGroup = service.createGameCharacterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            email: expect.any(Object),
            level: expect.any(Object),
            experience: expect.any(Object),
            shape: expect.any(Object),
            color: expect.any(Object),
            accessory: expect.any(Object),
            programmingLanguage: expect.any(Object),
            uniqueLink: expect.any(Object),
            progress: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });
    });

    describe('getGameCharacter', () => {
      it('should return NewGameCharacter for default GameCharacter initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createGameCharacterFormGroup(sampleWithNewData);

        const gameCharacter = service.getGameCharacter(formGroup) as any;

        expect(gameCharacter).toMatchObject(sampleWithNewData);
      });

      it('should return NewGameCharacter for empty GameCharacter initial value', () => {
        const formGroup = service.createGameCharacterFormGroup();

        const gameCharacter = service.getGameCharacter(formGroup) as any;

        expect(gameCharacter).toMatchObject({});
      });

      it('should return IGameCharacter', () => {
        const formGroup = service.createGameCharacterFormGroup(sampleWithRequiredData);

        const gameCharacter = service.getGameCharacter(formGroup) as any;

        expect(gameCharacter).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGameCharacter should not enable id FormControl', () => {
        const formGroup = service.createGameCharacterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGameCharacter should disable id FormControl', () => {
        const formGroup = service.createGameCharacterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
