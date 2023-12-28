import { Status } from 'app/entities/enumerations/status.model';

import { IProgress, NewProgress } from './progress.model';

export const sampleWithRequiredData: IProgress = {
  id: 58317,
  status: Status['IN_PROGRESS'],
  xp: 18212,
};

export const sampleWithPartialData: IProgress = {
  id: 85735,
  status: Status['COMPLETED'],
  xp: 99958,
};

export const sampleWithFullData: IProgress = {
  id: 94016,
  status: Status['STARTED'],
  currentLesson: 25810,
  xp: 6630,
};

export const sampleWithNewData: NewProgress = {
  status: Status['STARTED'],
  xp: 25103,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
