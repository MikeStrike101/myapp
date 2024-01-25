import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { GameCharacterDetailComponent } from './game-character-detail.component';

describe('GameCharacter Management Detail Component', () => {
  let comp: GameCharacterDetailComponent;
  let fixture: ComponentFixture<GameCharacterDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [GameCharacterDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ gameCharacter: { id: 123 } }) },
        },
        // Add any other necessary mock providers here
      ],
    })
      .overrideTemplate(GameCharacterDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GameCharacterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load gameCharacter on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.gameCharacter).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
