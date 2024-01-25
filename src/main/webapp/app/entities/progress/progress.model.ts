import { Status } from 'app/entities/enumerations/status.model';

export interface IProgress {
  id: number;
  status?: Status | null;
  currentLesson?: number | null;
  xp?: number | null;
}

export type NewProgress = Omit<IProgress, 'id'> & { id: null };
export interface IManualProgress extends Omit<IProgress, 'id'> {
  id: number;
}
