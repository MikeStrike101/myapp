import { ITestCase, NewTestCase } from './test-case.model';

export const sampleWithRequiredData: ITestCase = {
  id: 64710,
  input: 'Oregon Shoes invoice',
  output: 'Practical Legacy Minnesota',
};

export const sampleWithPartialData: ITestCase = {
  id: 32708,
  input: 'Sleek',
  output: 'Rapids',
};

export const sampleWithFullData: ITestCase = {
  id: 15086,
  input: 'Denmark auxiliary Grocery',
  output: 'Indiana',
};

export const sampleWithNewData: NewTestCase = {
  input: 'Forest',
  output: 'reboot AGP Intelligent',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
