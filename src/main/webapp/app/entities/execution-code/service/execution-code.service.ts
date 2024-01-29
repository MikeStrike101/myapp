import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExecutionCode, NewExecutionCode } from '../execution-code.model';

export type PartialUpdateExecutionCode = Partial<IExecutionCode> & Pick<IExecutionCode, 'id'>;

export type EntityResponseType = HttpResponse<IExecutionCode>;
export type EntityArrayResponseType = HttpResponse<IExecutionCode[]>;

@Injectable({ providedIn: 'root' })
export class ExecutionCodeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/execution-codes');
  protected resourceUrl2 = this.applicationConfigService.getEndpointFor('api/submit-code');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(executionCode: NewExecutionCode): Observable<EntityResponseType> {
    // console.log(executionCode);
    return this.http.post<IExecutionCode>(this.resourceUrl, executionCode, { observe: 'response' });
  }

  update(executionCode: IExecutionCode): Observable<EntityResponseType> {
    return this.http.put<IExecutionCode>(`${this.resourceUrl}/${this.getExecutionCodeIdentifier(executionCode)}`, executionCode, {
      observe: 'response',
    });
  }
  saveCode(executionCode: IExecutionCode): Observable<EntityResponseType> {
    if (executionCode.id) {
      return this.http.put<IExecutionCode>(`${this.resourceUrl}/${executionCode.id}`, executionCode, { observe: 'response' });
    } else {
      return this.http.post<IExecutionCode>(this.resourceUrl, executionCode, { observe: 'response' });
    }
  }
  partialUpdate(executionCode: PartialUpdateExecutionCode): Observable<EntityResponseType> {
    return this.http.patch<IExecutionCode>(`${this.resourceUrl}/${this.getExecutionCodeIdentifier(executionCode)}`, executionCode, {
      observe: 'response',
    });
  }
  submitCode(executionCodeDTO: NewExecutionCode): Observable<HttpResponse<string>> {
    return this.http.post<string>(`${this.resourceUrl2}`, executionCodeDTO, { observe: 'response' });
  }
  findByGameCharacterId(gameCharacterId: number): Observable<EntityArrayResponseType> {
    return this.http.get<IExecutionCode[]>(`${this.resourceUrl}/by-character/${gameCharacterId}`, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExecutionCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExecutionCode[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExecutionCodeIdentifier(executionCode: Pick<IExecutionCode, 'id'>): number {
    return executionCode.id;
  }

  compareExecutionCode(o1: Pick<IExecutionCode, 'id'> | null, o2: Pick<IExecutionCode, 'id'> | null): boolean {
    return o1 && o2 ? this.getExecutionCodeIdentifier(o1) === this.getExecutionCodeIdentifier(o2) : o1 === o2;
  }

  addExecutionCodeToCollectionIfMissing<Type extends Pick<IExecutionCode, 'id'>>(
    executionCodeCollection: Type[],
    ...executionCodesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const executionCodes: Type[] = executionCodesToCheck.filter(isPresent);
    if (executionCodes.length > 0) {
      const executionCodeCollectionIdentifiers = executionCodeCollection.map(
        executionCodeItem => this.getExecutionCodeIdentifier(executionCodeItem)!
      );
      const executionCodesToAdd = executionCodes.filter(executionCodeItem => {
        const executionCodeIdentifier = this.getExecutionCodeIdentifier(executionCodeItem);
        if (executionCodeCollectionIdentifiers.includes(executionCodeIdentifier)) {
          return false;
        }
        executionCodeCollectionIdentifiers.push(executionCodeIdentifier);
        return true;
      });
      return [...executionCodesToAdd, ...executionCodeCollection];
    }
    return executionCodeCollection;
  }
}
