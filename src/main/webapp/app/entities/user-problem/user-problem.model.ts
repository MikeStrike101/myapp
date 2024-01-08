import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IProblem } from 'app/entities/problem/problem.model';

export interface IUserProblem {
  id: number;
  solvedAt?: dayjs.Dayjs | null;
  passedTestCases?: number | null;
  xpAwarded?: number | null;
  user?: Pick<IUser, 'id'> | null;
  problem?: Pick<IProblem, 'id'> | null;
}

export type NewUserProblem = Omit<IUserProblem, 'id'> & { id: null };
