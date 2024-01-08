import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TestCaseDetailComponent } from './test-case-detail.component';

describe('TestCase Management Detail Component', () => {
  let comp: TestCaseDetailComponent;
  let fixture: ComponentFixture<TestCaseDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TestCaseDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ testCase: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TestCaseDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TestCaseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load testCase on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.testCase).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
