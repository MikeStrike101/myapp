import { IProblem } from 'app/entities/problem/problem.model';

export interface ITestCase {
  id: number;
  input?: string | null;
  output?: string | null;
  problem?: Pick<IProblem, 'id'> | null;
}

export type NewTestCase = Omit<ITestCase, 'id'> & { id: null };
