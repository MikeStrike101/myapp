import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CodeExecutionService } from 'app/entities/code-execution/code-execution.service';

import { IGameCharacter } from '../game-character.model';
import { ProblemService } from 'app/entities/problem/service/problem.service';
import * as ace from 'ace-builds';
import { ProgressService } from 'app/entities/progress/service/progress.service';
import { IProblem } from 'app/entities/problem/problem.model';
import { ExecutionCodeService } from 'app/entities/execution-code/service/execution-code.service';
import { IExecutionCode } from 'app/entities/execution-code/execution-code.model';
import 'ace-builds/src-noconflict/mode-python';
import 'ace-builds/src-noconflict/mode-java';

interface LanguageModeMapping {
  [language: string]: string | undefined;
}

@Component({
  selector: 'jhi-game-character-detail',
  templateUrl: './game-character-detail.component.html',
  styleUrls: ['./game-character-detail.component.scss'],
})
export class GameCharacterDetailComponent implements OnInit {
  gameCharacter: IGameCharacter | null = null;
  editor?: ace.Ace.Editor;
  editorCode = '';
  showQuestion = false;
  currentProblem: IProblem | null = null;
  currentExecutionCode: IExecutionCode | null = null;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected codeExecutionService: CodeExecutionService,
    protected progressService: ProgressService,
    protected problemService: ProblemService,
    private cdr: ChangeDetectorRef,
    protected executionCodeService: ExecutionCodeService
  ) {}

  setEditorMode(programmingLanguage: string | null): void {
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
                this.loadAndSetExecutionCode();
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
  initializeEditor(codeToLoad?: string): void {
    if (this.showQuestion && !this.editor && document.getElementById('editor')) {
      this.editor = ace.edit('editor');
      this.editor.getSession().on('change', () => {
        // Use optional chaining to prevent the error
        this.editorCode = this.editor?.getValue() ?? '';
      });

      const programmingLanguage = this.gameCharacter?.programmingLanguage ?? null;
      this.setEditorMode(programmingLanguage);

      if (codeToLoad) {
        this.editor.setValue(codeToLoad);
        this.editorCode = codeToLoad;
      }
      this.loadAndSetExecutionCode();
    }
  }

  getEditorModeFromLanguage(language: string): string | undefined {
    const languageModeMapping: LanguageModeMapping = {
      Python: 'python',
      Java: 'java',
    };
    return languageModeMapping[language];
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gameCharacter }) => {
      this.gameCharacter = gameCharacter;
      this.loadCurrentQuestionAndEditor();
    });
  }

  previousState(): void {
    window.history.back();
  }

  loadCurrentQuestionAndEditor(): void {
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
                this.loadAndSetExecutionCode();
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

  loadAndSetExecutionCode(): void {
    const gameCharacterId = this.gameCharacter?.id;
    const currentProblemId = this.currentProblem?.id;

    if (gameCharacterId && currentProblemId) {
      this.executionCodeService.findByGameCharacterId(gameCharacterId).subscribe({
        next: response => {
          const executionCodes = response.body;
          const matchingCode = executionCodes?.find(ec => ec.questionNumber === currentProblemId);

          if (matchingCode?.code != null) {
            this.currentExecutionCode = matchingCode;
            this.editor?.setValue(matchingCode.code); // Set the code in the editor using optional chaining
            this.editorCode = matchingCode.code; // Update the local editorCode variable
          }
        },
        error: err => {
          console.error('Error retrieving execution codes:', err);
        },
      });
    }
  }

  saveCode(): void {
    if (this.currentExecutionCode && this.editorCode) {
      // Update the code property of the currentExecutionCode object
      const updatedExecutionCode: IExecutionCode = {
        ...this.currentExecutionCode,
        code: this.editorCode,
      };

      this.executionCodeService.saveCode(updatedExecutionCode).subscribe({
        // eslint-disable-next-line no-console
        next: () => console.log('Code saved successfully'),
        error: err => console.error('Failed to save code', err),
      });
    } else {
      console.error('Cannot save the code. Make sure the execution code and editor are properly initialized.');
    }
  }

  runCode(): void {
    const language = 'python';
    const version = '3.10.0';
    const code = this.editorCode;

    this.codeExecutionService.executeCode(code, language, version).subscribe(
      response => {
        // eslint-disable-next-line no-console
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
