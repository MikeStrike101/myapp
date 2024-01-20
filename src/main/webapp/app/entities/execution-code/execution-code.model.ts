export interface IExecutionCode {
  id: number;
  questionNumber?: number | null;
  code?: string | null;
  gameCharacter?: number | null;
}

export type NewExecutionCode = Omit<IExecutionCode, 'id'> & { id: null };
