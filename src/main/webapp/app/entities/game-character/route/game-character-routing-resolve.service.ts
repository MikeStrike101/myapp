import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGameCharacter } from '../game-character.model';
import { GameCharacterService } from '../service/game-character.service';

@Injectable({ providedIn: 'root' })
export class GameCharacterRoutingResolveService implements Resolve<IGameCharacter | null> {
  constructor(protected service: GameCharacterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGameCharacter | null | never> {
    const id = route.params['id'];
    const uniqueLink = route.params['uniqueLink'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gameCharacter: HttpResponse<IGameCharacter>) => {
          if (gameCharacter.body) {
            return of(gameCharacter.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    if (uniqueLink) {
      return this.service.findByUniqueLink(uniqueLink).pipe(
        mergeMap((gameCharacter: HttpResponse<IGameCharacter>) => {
          if (gameCharacter.body) {
            return of(gameCharacter.body);
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
