import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGameCharacter, NewGameCharacter } from '../game-character.model';

export type PartialUpdateGameCharacter = Partial<IGameCharacter> & Pick<IGameCharacter, 'id'>;

export type EntityResponseType = HttpResponse<IGameCharacter>;
export type EntityArrayResponseType = HttpResponse<IGameCharacter[]>;

@Injectable({ providedIn: 'root' })
export class GameCharacterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/game-characters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gameCharacter: NewGameCharacter): Observable<EntityResponseType> {
    return this.http.post<IGameCharacter>(this.resourceUrl, gameCharacter, { observe: 'response' });
  }

  update(gameCharacter: IGameCharacter): Observable<EntityResponseType> {
    return this.http.put<IGameCharacter>(`${this.resourceUrl}/${this.getGameCharacterIdentifier(gameCharacter)}`, gameCharacter, {
      observe: 'response',
    });
  }

  partialUpdate(gameCharacter: PartialUpdateGameCharacter): Observable<EntityResponseType> {
    return this.http.patch<IGameCharacter>(`${this.resourceUrl}/${this.getGameCharacterIdentifier(gameCharacter)}`, gameCharacter, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGameCharacter>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGameCharacter[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGameCharacterIdentifier(gameCharacter: Pick<IGameCharacter, 'id'>): number {
    if (gameCharacter.id === null) {
      throw new Error('Cannot retrieve identifier for gameCharacter with null ID.');
    }
    return gameCharacter.id;
  }

  compareGameCharacter(o1: Pick<IGameCharacter, 'id'> | null, o2: Pick<IGameCharacter, 'id'> | null): boolean {
    return o1 && o2 ? this.getGameCharacterIdentifier(o1) === this.getGameCharacterIdentifier(o2) : o1 === o2;
  }

  addGameCharacterToCollectionIfMissing<Type extends Pick<IGameCharacter, 'id'>>(
    gameCharacterCollection: Type[],
    ...gameCharactersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const gameCharacters: Type[] = gameCharactersToCheck.filter(isPresent);
    if (gameCharacters.length > 0) {
      const gameCharacterCollectionIdentifiers = gameCharacterCollection.map(
        gameCharacterItem => this.getGameCharacterIdentifier(gameCharacterItem)!
      );
      const gameCharactersToAdd = gameCharacters.filter(gameCharacterItem => {
        const gameCharacterIdentifier = this.getGameCharacterIdentifier(gameCharacterItem);
        if (gameCharacterCollectionIdentifiers.includes(gameCharacterIdentifier)) {
          return false;
        }
        gameCharacterCollectionIdentifiers.push(gameCharacterIdentifier);
        return true;
      });
      return [...gameCharactersToAdd, ...gameCharacterCollection];
    }
    return gameCharacterCollection;
  }
}
