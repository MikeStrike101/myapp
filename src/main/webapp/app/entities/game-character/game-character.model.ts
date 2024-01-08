import { IProgress } from 'app/entities/progress/progress.model';
import { IUser } from 'app/entities/user/user.model';

export interface IGameCharacter {
  id: number | null;
  name?: string | null;
  email?: string | null;
  level?: number | null;
  experience?: number | null;
  shape?: string | null;
  color?: string | null;
  accessory?: string | null;
  programmingLanguage?: string | null;
  uniqueLink?: string | null;
  profilePicture?: string | null;
  progress?: Pick<IProgress, 'id'> | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewGameCharacter = Omit<IGameCharacter, 'id'> & { id: null };
