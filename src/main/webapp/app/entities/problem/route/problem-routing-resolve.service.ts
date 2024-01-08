import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProblem } from '../problem.model';
import { ProblemService } from '../service/problem.service';

@Injectable({ providedIn: 'root' })
export class ProblemRoutingResolveService implements Resolve<IProblem | null> {
  constructor(protected service: ProblemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProblem | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((problem: HttpResponse<IProblem>) => {
          if (problem.body) {
            return of(problem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
