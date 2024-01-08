import { IProblem, NewProblem } from './problem.model';

export const sampleWithRequiredData: IProblem = {
  id: 78437,
  title: 'Frozen Liaison bluetooth',
  description: 'Faroe FundamentalXXX',
  level: 7930,
  xpReward: 25683,
};

export const sampleWithPartialData: IProblem = {
  id: 78787,
  title: 'transmitter pixel',
  description: 'transmittingXXXXXXXX',
  level: 42805,
  xpReward: 43409,
};

export const sampleWithFullData: IProblem = {
  id: 99358,
  title: '1080p Thailand',
  description: 'Handmade productXXXX',
  level: 30054,
  xpReward: 22628,
};

export const sampleWithNewData: NewProblem = {
  title: 'transmitting payment Jordan',
  description: 'Table backing Customer',
  level: 45592,
  xpReward: 19116,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
