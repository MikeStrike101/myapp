import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProblem } from '../problem.model';

@Component({
  selector: 'jhi-problem-detail',
  templateUrl: './problem-detail.component.html',
})
export class ProblemDetailComponent implements OnInit {
  problem: IProblem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ problem }) => {
      this.problem = problem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
