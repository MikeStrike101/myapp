import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProblem, NewProblem } from '../problem.model';

export type PartialUpdateProblem = Partial<IProblem> & Pick<IProblem, 'id'>;

export type EntityResponseType = HttpResponse<IProblem>;
export type EntityArrayResponseType = HttpResponse<IProblem[]>;

@Injectable({ providedIn: 'root' })
export class ProblemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/problems');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(problem: NewProblem): Observable<EntityResponseType> {
    return this.http.post<IProblem>(this.resourceUrl, problem, { observe: 'response' });
  }

  update(problem: IProblem): Observable<EntityResponseType> {
    return this.http.put<IProblem>(`${this.resourceUrl}/${this.getProblemIdentifier(problem)}`, problem, { observe: 'response' });
  }

  partialUpdate(problem: PartialUpdateProblem): Observable<EntityResponseType> {
    return this.http.patch<IProblem>(`${this.resourceUrl}/${this.getProblemIdentifier(problem)}`, problem, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProblem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProblem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProblemIdentifier(problem: Pick<IProblem, 'id'>): number {
    return problem.id;
  }

  compareProblem(o1: Pick<IProblem, 'id'> | null, o2: Pick<IProblem, 'id'> | null): boolean {
    return o1 && o2 ? this.getProblemIdentifier(o1) === this.getProblemIdentifier(o2) : o1 === o2;
  }

  addProblemToCollectionIfMissing<Type extends Pick<IProblem, 'id'>>(
    problemCollection: Type[],
    ...problemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const problems: Type[] = problemsToCheck.filter(isPresent);
    if (problems.length > 0) {
      const problemCollectionIdentifiers = problemCollection.map(problemItem => this.getProblemIdentifier(problemItem)!);
      const problemsToAdd = problems.filter(problemItem => {
        const problemIdentifier = this.getProblemIdentifier(problemItem);
        if (problemCollectionIdentifiers.includes(problemIdentifier)) {
          return false;
        }
        problemCollectionIdentifiers.push(problemIdentifier);
        return true;
      });
      return [...problemsToAdd, ...problemCollection];
    }
    return problemCollection;
  }
}
