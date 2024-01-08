import dayjs from 'dayjs/esm';

import { IUserProblem, NewUserProblem } from './user-problem.model';

export const sampleWithRequiredData: IUserProblem = {
  id: 29173,
  passedTestCases: 75714,
  xpAwarded: 37811,
};

export const sampleWithPartialData: IUserProblem = {
  id: 76385,
  passedTestCases: 46731,
  xpAwarded: 2684,
};

export const sampleWithFullData: IUserProblem = {
  id: 85983,
  solvedAt: dayjs('2024-01-05T17:19'),
  passedTestCases: 32227,
  xpAwarded: 17998,
};

export const sampleWithNewData: NewUserProblem = {
  passedTestCases: 40408,
  xpAwarded: 76927,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
