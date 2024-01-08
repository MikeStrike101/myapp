export interface IProblem {
  id: number;
  title?: string | null;
  description?: string | null;
  level?: number | null;
  xpReward?: number | null;
}

export type NewProblem = Omit<IProblem, 'id'> & { id: null };
