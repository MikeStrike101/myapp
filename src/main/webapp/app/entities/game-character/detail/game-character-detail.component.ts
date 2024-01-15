import { Component, OnInit, AfterViewInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CodeExecutionService } from 'app/entities/code-execution/code-execution.service';

import { IGameCharacter } from '../game-character.model';
import { ProblemService } from 'app/entities/problem/service/problem.service';
import * as ace from 'ace-builds';
interface LanguageModeMapping {
  [language: string]: string | undefined; // This is the index signature
}

@Component({
  selector: 'jhi-game-character-detail',
  templateUrl: './game-character-detail.component.html',
  styleUrls: ['./game-character-detail.component.scss'],
})
export class GameCharacterDetailComponent implements OnInit, AfterViewInit {
  gameCharacter: IGameCharacter | null = null;
  editor?: ace.Ace.Editor;
  editorCode: string = '';

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected problemService: ProblemService,
    protected codeExecutionService: CodeExecutionService
  ) {}

  ngAfterViewInit(): void {
    this.editor = ace.edit('editor');
    this.editor.getSession().on('change', () => {
      if (this.editor != null) this.editorCode = this.editor.getValue();
    });

    const programmingLanguage = this.gameCharacter?.programmingLanguage ?? null;
    this.setEditorMode(programmingLanguage);
    // Additional editor configurations
  }

  setEditorMode(programmingLanguage: string | null) {
    if (programmingLanguage) {
      const editorMode = this.getEditorModeFromLanguage(programmingLanguage);
      if (editorMode) {
        this.editor!.session.setMode(`ace/mode/${editorMode}`);
      }
    }
  }

  getEditorModeFromLanguage(language: string): string | undefined {
    // Allow undefined as a return type
    const languageModeMapping: LanguageModeMapping = {
      JavaScript: 'javascript',
      Python: 'python',
      // Add other languages and their corresponding Ace modes here
    };
    return languageModeMapping[language];
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gameCharacter }) => {
      this.gameCharacter = gameCharacter;
      if (this.editor && this.gameCharacter) {
        const programmingLanguage = this.gameCharacter.programmingLanguage ?? null;
        this.setEditorMode(programmingLanguage);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  runCode(): void {
    const language = 'python'; // For example, determine this dynamically
    const version = '3.10.0'; // For example, determine this dynamically
    const code = this.editorCode; // The code from your code editor

    this.codeExecutionService.executeCode(code, language, version).subscribe(
      response => {
        console.log('Compiler output:', response);
        // Handle the successful response here
      },
      error => {
        console.error('Error executing code:', error);
        // Handle the error here
      }
    );
  }
}
