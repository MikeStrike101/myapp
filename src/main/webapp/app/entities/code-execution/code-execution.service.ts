import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({
  providedIn: 'root',
})
export class CodeExecutionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/execute-code');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

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
    return this.http.post<any>(this.resourceUrl, payload);
  }
}
