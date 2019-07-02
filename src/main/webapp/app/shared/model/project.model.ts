import { Moment } from 'moment';

export interface IProject {
  id?: number;
  name?: string;
  date?: Moment;
  description?: string;
}

export class Project implements IProject {
  constructor(public id?: number, public name?: string, public date?: Moment, public description?: string) {}
}
