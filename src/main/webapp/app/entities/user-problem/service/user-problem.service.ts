import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserProblem, NewUserProblem } from '../user-problem.model';

export type PartialUpdateUserProblem = Partial<IUserProblem> & Pick<IUserProblem, 'id'>;

type RestOf<T extends IUserProblem | NewUserProblem> = Omit<T, 'solvedAt'> & {
  solvedAt?: string | null;
};

export type RestUserProblem = RestOf<IUserProblem>;

export type NewRestUserProblem = RestOf<NewUserProblem>;

export type PartialUpdateRestUserProblem = RestOf<PartialUpdateUserProblem>;

export type EntityResponseType = HttpResponse<IUserProblem>;
export type EntityArrayResponseType = HttpResponse<IUserProblem[]>;

@Injectable({ providedIn: 'root' })
export class UserProblemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-problems');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userProblem: NewUserProblem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userProblem);
    return this.http
      .post<RestUserProblem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(userProblem: IUserProblem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userProblem);
    return this.http
      .put<RestUserProblem>(`${this.resourceUrl}/${this.getUserProblemIdentifier(userProblem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(userProblem: PartialUpdateUserProblem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userProblem);
    return this.http
      .patch<RestUserProblem>(`${this.resourceUrl}/${this.getUserProblemIdentifier(userProblem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUserProblem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUserProblem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserProblemIdentifier(userProblem: Pick<IUserProblem, 'id'>): number {
    return userProblem.id;
  }

  compareUserProblem(o1: Pick<IUserProblem, 'id'> | null, o2: Pick<IUserProblem, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserProblemIdentifier(o1) === this.getUserProblemIdentifier(o2) : o1 === o2;
  }

  addUserProblemToCollectionIfMissing<Type extends Pick<IUserProblem, 'id'>>(
    userProblemCollection: Type[],
    ...userProblemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userProblems: Type[] = userProblemsToCheck.filter(isPresent);
    if (userProblems.length > 0) {
      const userProblemCollectionIdentifiers = userProblemCollection.map(
        userProblemItem => this.getUserProblemIdentifier(userProblemItem)!
      );
      const userProblemsToAdd = userProblems.filter(userProblemItem => {
        const userProblemIdentifier = this.getUserProblemIdentifier(userProblemItem);
        if (userProblemCollectionIdentifiers.includes(userProblemIdentifier)) {
          return false;
        }
        userProblemCollectionIdentifiers.push(userProblemIdentifier);
        return true;
      });
      return [...userProblemsToAdd, ...userProblemCollection];
    }
    return userProblemCollection;
  }

  protected convertDateFromClient<T extends IUserProblem | NewUserProblem | PartialUpdateUserProblem>(userProblem: T): RestOf<T> {
    return {
      ...userProblem,
      solvedAt: userProblem.solvedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUserProblem: RestUserProblem): IUserProblem {
    return {
      ...restUserProblem,
      solvedAt: restUserProblem.solvedAt ? dayjs(restUserProblem.solvedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUserProblem>): HttpResponse<IUserProblem> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUserProblem[]>): HttpResponse<IUserProblem[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
