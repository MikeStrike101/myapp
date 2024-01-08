import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserProblem } from '../user-problem.model';

@Component({
  selector: 'jhi-user-problem-detail',
  templateUrl: './user-problem-detail.component.html',
})
export class UserProblemDetailComponent implements OnInit {
  userProblem: IUserProblem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userProblem }) => {
      this.userProblem = userProblem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
