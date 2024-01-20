import { IExecutionCode, NewExecutionCode } from './execution-code.model';

export const sampleWithRequiredData: IExecutionCode = {
  id: 27502,
  questionNumber: 12488,
  code: '../fake-data/blob/hipster.txt',
  gameCharacter: 26413,
};

export const sampleWithPartialData: IExecutionCode = {
  id: 63865,
  questionNumber: 65011,
  code: '../fake-data/blob/hipster.txt',
  gameCharacter: 54027,
};

export const sampleWithFullData: IExecutionCode = {
  id: 97584,
  questionNumber: 79545,
  code: '../fake-data/blob/hipster.txt',
  gameCharacter: 74028,
};

export const sampleWithNewData: NewExecutionCode = {
  questionNumber: 88956,
  code: '../fake-data/blob/hipster.txt',
  gameCharacter: 9784,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
