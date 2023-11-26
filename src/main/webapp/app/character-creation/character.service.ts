import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CharacterService {
  private apiUrl = 'http://api-url.com/characters'; // Replace with your API URL

  constructor(private http: HttpClient) {}

  // Method to create a new character
  createCharacter(name: string, features: any): Observable<any> {
    const characterData = { name, features };
    return this.http.post(this.apiUrl, characterData);
  }

  // Method to fetch character details by name
  getCharacterByName(name: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${name}`);
  }

  // Add more methods as needed for your application
}
