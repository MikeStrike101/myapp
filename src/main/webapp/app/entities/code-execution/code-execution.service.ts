import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CodeExecutionService {
  private backendUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  executeCode(code: string, language: string, version: string): Observable<any> {
    const payload = {
      language: language,
      version: version,
      files: [
        {
          content: code,
        },
      ],
    };
    return this.http.post<any>(`${this.backendUrl}/execute-code`, payload);
  }
}
