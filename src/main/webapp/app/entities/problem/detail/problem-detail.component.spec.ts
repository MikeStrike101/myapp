import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProblemDetailComponent } from './problem-detail.component';

describe('Problem Management Detail Component', () => {
  let comp: ProblemDetailComponent;
  let fixture: ComponentFixture<ProblemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProblemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ problem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProblemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProblemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load problem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.problem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
