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
  id: 98680,
  name: 'Account',
  email: 'Grant.Wisoky47@gmail.com',
  level: 3214,
  experience: 83347,
  shape: 'mobile',
  color: 'teal',
  accessory: 'Account visualize Berkshire',
  programmingLanguage: 'Bermuda Small Soft',
  uniqueLink: 'Krona Tajikistan',
};

export const sampleWithFullData: IGameCharacter = {
  id: 87220,
  name: '(Slovak',
  email: 'Tyler41@yahoo.com',
  level: 30663,
  experience: 16351,
  shape: 'communities Slovakia',
  color: 'yellow',
  accessory: 'cross-platform Viaduct Handmade',
  programmingLanguage: 'Operative Legacy',
  uniqueLink: 'maroon neutral',
};

export const sampleWithNewData: NewGameCharacter = {
  name: 'Lodge United Cambridgeshire',
  email: 'Mose.Grimes@gmail.com',
  level: 41933,
  experience: 73971,
  shape: 'Mouse card alliance',
  color: 'white',
  programmingLanguage: 'deposit Beauty',
  uniqueLink: 'invoice embrace',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
