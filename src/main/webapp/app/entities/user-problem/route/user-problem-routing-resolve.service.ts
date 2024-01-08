import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserProblem } from '../user-problem.model';
import { UserProblemService } from '../service/user-problem.service';

@Injectable({ providedIn: 'root' })
export class UserProblemRoutingResolveService implements Resolve<IUserProblem | null> {
  constructor(protected service: UserProblemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserProblem | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userProblem: HttpResponse<IUserProblem>) => {
          if (userProblem.body) {
            return of(userProblem.body);
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
