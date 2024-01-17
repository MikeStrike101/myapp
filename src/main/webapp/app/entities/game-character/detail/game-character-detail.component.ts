import { Component, OnInit, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CodeExecutionService } from 'app/entities/code-execution/code-execution.service';

import { IGameCharacter } from '../game-character.model';
import { ProblemService } from 'app/entities/problem/service/problem.service';
import * as ace from 'ace-builds';
import { ProgressService } from 'app/entities/progress/service/progress.service';
import { IProblem } from 'app/entities/problem/problem.model';
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
  showQuestion = false; // Flag to indicate whether to show the question
  currentProblem: IProblem | null = null; // The current problem data

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected codeExecutionService: CodeExecutionService,
    protected progressService: ProgressService,
    protected problemService: ProblemService,
    private cdr: ChangeDetectorRef
  ) {}

  ngAfterViewInit(): void {}

  setEditorMode(programmingLanguage: string | null) {
    if (programmingLanguage) {
      const editorMode = this.getEditorModeFromLanguage(programmingLanguage);
      if (editorMode) {
        this.editor!.session.setMode(`ace/mode/${editorMode}`);
      }
    }
  }

  displayQuestion(): void {
    if (this.gameCharacter?.id) {
      this.progressService.find(this.gameCharacter.id).subscribe(
        progressResponse => {
          const currentQuestionId = progressResponse.body?.currentLesson;
          if (currentQuestionId) {
            this.problemService.find(currentQuestionId).subscribe(
              problemResponse => {
                this.currentProblem = problemResponse.body;
                this.showQuestion = true;
                this.cdr.detectChanges();
                this.initializeEditor();
              },
              error => {
                console.error('Error fetching problem details:', error);
              }
            );
          }
        },
        error => {
          console.error('Error fetching progress:', error);
        }
      );
    }
  }
  initializeEditor(): void {
    if (this.showQuestion && !this.editor && document.getElementById('editor')) {
      this.editor = ace.edit('editor');
      this.editor.getSession().on('change', () => {
        if (this.editor != null) this.editorCode = this.editor.getValue();
      });

      const programmingLanguage = this.gameCharacter?.programmingLanguage ?? null;
      this.setEditorMode(programmingLanguage);

      // ... other editor configurations ...
    }
  }

  getEditorModeFromLanguage(language: string): string | undefined {
    const languageModeMapping: LanguageModeMapping = {
      JavaScript: 'javascript',
      Python: 'python',
    };
    return languageModeMapping[language];
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gameCharacter }) => {
      this.gameCharacter = gameCharacter;
    });
  }

  previousState(): void {
    window.history.back();
  }

  runCode(): void {
    const language = 'python';
    const version = '3.10.0';
    const code = this.editorCode;

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
