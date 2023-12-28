import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IGameCharacter } from '../entities/game-character/game-character.model';

@Injectable({
  providedIn: 'root',
})
export class CharacterService {
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  private gameCharactersUrl = 'http://localhost:8080/api/game-characters';

  constructor(private http: HttpClient) {}

  createGameCharacter(gameCharacter: IGameCharacter): Observable<IGameCharacter> {
    return this.http.post<IGameCharacter>(this.gameCharactersUrl, gameCharacter, this.httpOptions);
  }

  // Retrieve all game characters
  getGameCharacters(): Observable<IGameCharacter[]> {
    return this.http.get<IGameCharacter[]>(this.gameCharactersUrl);
  }

  // Retrieve a single game character by ID
  getGameCharacterById(id: number): Observable<IGameCharacter> {
    const url = `${this.gameCharactersUrl}/${id}`;
    return this.http.get<IGameCharacter>(url);
  }

  // Partial update for a game character
  partialUpdateGameCharacter(id: number, patchData: any): Observable<IGameCharacter> {
    const url = `${this.gameCharactersUrl}/${id}`;
    return this.http.patch<IGameCharacter>(url, patchData, this.httpOptions);
  }

  // Delete a game character
  deleteGameCharacter(id: number): Observable<{}> {
    const url = `${this.gameCharactersUrl}/${id}`;
    return this.http.delete(url, this.httpOptions);
  }
}
