// update-progress-request.model.ts
export interface UpdateProgressRequest {
  gameCharacterId: number;
  nextQuestionNumber: number;
  newXP: number;
  newLevel: number;
}
