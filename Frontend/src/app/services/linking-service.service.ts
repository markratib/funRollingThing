import { Injectable } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { transferObject } from '../tansferObject';

@Injectable({
  providedIn: 'root'
})
//I had created this to get the data from the parent component (rollInput) to the child
//component (rollResults). But my true problem was attempting to call a function in the child
//from the parent. This is not gonig to be used any longer as I have realized it does not do
//what I want it to.
export class LinkingServiceService {

  constructor() { }

  private  rollResults = new ReplaySubject<transferObject>;

  public addResults = (value : transferObject): void => this.rollResults.next(value);

  public getResults = this.rollResults.asObservable();
}
