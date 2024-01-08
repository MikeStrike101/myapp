import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITestCase } from '../test-case.model';

@Component({
  selector: 'jhi-test-case-detail',
  templateUrl: './test-case-detail.component.html',
})
export class TestCaseDetailComponent implements OnInit {
  testCase: ITestCase | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ testCase }) => {
      this.testCase = testCase;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
