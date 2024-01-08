import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UserProblemDetailComponent } from './user-problem-detail.component';

describe('UserProblem Management Detail Component', () => {
  let comp: UserProblemDetailComponent;
  let fixture: ComponentFixture<UserProblemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserProblemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ userProblem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UserProblemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UserProblemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load userProblem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.userProblem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
