import { IGameCharacter, NewGameCharacter } from './game-character.model';

export const sampleWithRequiredData: IGameCharacter = {
  id: 35492,
  name: 'Streamlined',
  email: 'Andrew3@yahoo.com',
  level: 91961,
  experience: 4964,
  shape: 'Ergonomic Handcrafted',
  color: 'yellow',
  programmingLanguage: 'extensible Books',
  uniqueLink: 'Crest',
};

export const sampleWithPartialData: IGameCharacter = {
  id: 9499,
  name: 'Credit productize',
  email: 'Jarrell.Bartoletti79@yahoo.com',
  level: 94831,
  experience: 15950,
  shape: 'pixel',
  color: 'sky blue',
  accessory: 'Kwanza real-time',
  programmingLanguage: 'Licensed Sports synthesize',
  uniqueLink: 'engage HDD Beauty',
  profilePicture: 'Cambridgeshire Cotton',
};

export const sampleWithFullData: IGameCharacter = {
  id: 16351,
  name: 'communities Slovakia',
  email: 'Syble_Schiller@gmail.com',
  level: 44874,
  experience: 93138,
  shape: 'Handmade Bedfordshire',
  color: 'ivory',
  accessory: 'program maroon neutral',
  programmingLanguage: 'Lodge United Cambridgeshire',
  uniqueLink: 'card',
  profilePicture: 'deliverables',
};

export const sampleWithNewData: NewGameCharacter = {
  name: 'green',
  email: 'Constantin50@gmail.com',
  level: 23469,
  experience: 60008,
  shape: 'Integrated Borders',
  color: 'olive',
  programmingLanguage: 'exuding Focused synthesize',
  uniqueLink: 'Metal Won',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
