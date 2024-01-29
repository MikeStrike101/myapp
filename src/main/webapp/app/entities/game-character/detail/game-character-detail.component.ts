import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CodeExecutionService } from 'app/entities/code-execution/code-execution.service';
import isEqual from 'lodash/isEqual';

import { IGameCharacter } from '../game-character.model';
import { ProblemService } from 'app/entities/problem/service/problem.service';
import * as ace from 'ace-builds';
import { ProgressService } from 'app/entities/progress/service/progress.service';
import { IProblem } from 'app/entities/problem/problem.model';
import { ExecutionCodeService } from 'app/entities/execution-code/service/execution-code.service';
import { IExecutionCode, NewExecutionCode } from 'app/entities/execution-code/execution-code.model';
import 'ace-builds/src-noconflict/mode-python';
import 'ace-builds/src-noconflict/mode-java';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IProgress } from 'app/entities/progress/progress.model';
import { Observable } from 'rxjs';
import { UpdateProgressRequest } from './update-progress-request.model';

interface LanguageModeMapping {
  [language: string]: string | undefined;
}

interface CodeSubmissionResponse {
  message: string;
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
  showSuccessMessage = false;

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
            this.editor?.setValue(matchingCode.code);
            this.editorCode = matchingCode.code;
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
    const language = this.gameCharacter?.programmingLanguage || 'python';
    const version = '3.10.0';
    const code = this.editorCode;
    const gameCharacterId = this.gameCharacter?.id;
    const questionNumber = this.currentProblem?.id;
    const xpReward = this.currentProblem?.xpReward;
    const level = this.currentProblem?.level;
    const currentExperience = this.gameCharacter?.experience;

    if (gameCharacterId && questionNumber && currentExperience != null && xpReward != null && level != null) {
      const executionCode: NewExecutionCode = {
        id: null,
        code: code,
        gameCharacter: gameCharacterId,
        questionNumber: questionNumber,
      };

      this.executionCodeService.submitCode(executionCode).subscribe({
        next: httpResponse => {
          const responseBody = httpResponse.body;
          console.log('Code submission response:', responseBody);
          if (isEqual(responseBody, { message: 'Code executed successfully. Test case passed!' })) {
            this.showSuccessMessage = true;
          } else {
            console.error('Test case did not pass:', httpResponse.body);
          }
        },
        error: error => {
          console.error('Error submitting code:', error);
        },
      });
    } else {
      console.error('Game character ID or question number is missing');
    }
  }

  continueProgress(): void {
    const gameCharacterId = this.gameCharacter?.id;
    const nextQuestionNumber = this.currentProblem?.id! + 1;
    const newXP = this.gameCharacter?.experience! + this.currentProblem?.xpReward!;
    const newLevel = this.currentProblem?.level!;

    if (gameCharacterId && nextQuestionNumber && newXP != null && newLevel != null) {
      const updateRequest: UpdateProgressRequest = {
        gameCharacterId,
        nextQuestionNumber,
        newXP,
        newLevel,
      };

      this.progressService.updateProgress(updateRequest).subscribe({
        next: response => {
          console.log('Progress updated successfully', response);
          this.showSuccessMessage = false; // Hide success message and 'Continue' button
          const currentUrl = this.router.url;
          this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
            this.router.navigate([currentUrl]); // navigate to the same url
          });
        },
        error: error => {
          console.error('Failed to update progress', error);
          // Show error message to the user
        },
      });
    } else {
      console.error('Required parameters for progress update are missing');
      // Show error message to the user
    }
  }

  updateUserProgress(
    gameCharacterId: number,
    newLessonNumber: number,
    newXP: number,
    newLevel: number
  ): Observable<HttpResponse<IProgress>> {
    const updatedProgress: IProgress = {
      id: gameCharacterId,
      currentLesson: newLessonNumber,
      xp: newXP,
    };

    return this.progressService.update(updatedProgress);
  }
}
