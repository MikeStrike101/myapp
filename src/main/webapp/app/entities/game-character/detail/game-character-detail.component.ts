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
import { UpdateProgressRequest } from './update-progress-request.model';
import { GameCharacterService } from '../service/game-character.service';

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
  showSuccessMessage = false;
  isLoading = false;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected codeExecutionService: CodeExecutionService,
    protected progressService: ProgressService,
    protected problemService: ProblemService,
    private cdr: ChangeDetectorRef,
    protected executionCodeService: ExecutionCodeService,
    protected gameCharacterService: GameCharacterService
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
    const language = this.gameCharacter?.programmingLanguage ?? 'python'; // Use nullish coalescing
    // 'version' variable is removed since it's not used
    const code = this.editorCode;
    const gameCharacterId = this.gameCharacter?.id;
    const questionNumber = this.currentProblem?.id;
    const xpReward = this.currentProblem?.xpReward;
    const level = this.currentProblem?.level;
    const currentExperience = this.gameCharacter?.experience;

    if (gameCharacterId && questionNumber && currentExperience != null && xpReward != null && level != null) {
      const executionCode: NewExecutionCode = {
        id: null,
        code, // Use property shorthand
        gameCharacter: gameCharacterId,
        questionNumber, // Use property shorthand
      };

      this.executionCodeService.submitCode(executionCode).subscribe({
        next: httpResponse => {
          const responseBody = httpResponse.body;
          // Removed console.log for production, or you can disable the rule in your ESLint config
          if (isEqual(responseBody, { message: 'Code executed successfully. Test case passed!' })) {
            this.showSuccessMessage = true;
          } else {
            // Removed console.error for production, or you can disable the rule in your ESLint config
          }
        },
        error: error => {
          // Removed console.error for production, or you can disable the rule in your ESLint config
        },
      });
    } else {
      // Removed console.error for production, or you can disable the rule in your ESLint config
    }
  }

  continueProgress(): void {
    this.isLoading = true;
    const gameCharacterId = this.gameCharacter?.id;
    const nextQuestionNumber = this.currentProblem?.id! + 1;
    const newXP = this.gameCharacter?.experience! + this.currentProblem?.xpReward!;
    const newLevel = this.currentProblem?.level!;

    if (gameCharacterId && nextQuestionNumber && newXP != null && newLevel != null) {
      if (
        this.gameCharacter?.level !== newLevel &&
        this.gameCharacter?.shape &&
        this.gameCharacter?.color &&
        this.gameCharacter?.accessory &&
        this.gameCharacter?.name
      ) {
        this.gameCharacterService.generateNewImage(this.gameCharacter).subscribe({
          next: imageResponse => {
            if (this.gameCharacter) {
              this.gameCharacter.profilePicture = imageResponse.body.filename;
            }

            this.updateUserProgress(gameCharacterId, nextQuestionNumber, newXP, newLevel, true);
          },
          error: imageError => {
            console.error('Failed to generate new image', imageError);
            this.finishLoading();
          },
        });
      } else {
        this.updateUserProgress(gameCharacterId, nextQuestionNumber, newXP, newLevel, false);
      }
    } else {
      console.error('Required parameters for progress update are missing');
      this.isLoading = false;
    }
  }

  updateUserProgress(gameCharacterId: number, nextQuestionNumber: number, newXP: number, newLevel: number, shouldRefresh: boolean): void {
    const updateRequest: UpdateProgressRequest = {
      gameCharacterId,
      nextQuestionNumber,
      newXP,
      newLevel,
    };

    this.progressService.updateProgress(updateRequest).subscribe({
      next: () => {
        this.showSuccessMessage = false;

        if (shouldRefresh) {
          window.location.reload();
        } else {
          this.finishLoading();
        }
      },
      error: error => {
        console.error('Failed to update progress', error);
        this.finishLoading();
      },
    });
  }

  private finishLoading(): void {
    this.isLoading = false;
    const currentUrl = this.router.url;
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this.router.navigate([currentUrl]);
    });
  }
}
