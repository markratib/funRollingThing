import { Inject, Injectable, ɵisObservable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LinkingServiceService } from './linking-service.service';

@Injectable({
  providedIn: 'root'
})
export class DiceServiceService {

  url?: string;
  constructor(private http: HttpClient, private linkService: LinkingServiceService) { }

  //This is based off what the chatbot told me to do. I stopped using it.
  async rollDice(size: number, numdice: number, rolls: number): Promise<number[]>
  {
    
    try
    {
      const fullUrl = `${this.url}size=${size}&numDice=${numdice}&rolls=${rolls}`;
      const response = await fetch(fullUrl, {
        method: "POST"
      });
      const result = await response.json();
      return result;
    } catch(e:any)
    {
      throw new Error(e.message);
    }
  }

  rollDice2(size: number, numdice: number, rolls: number): Observable<number[]>{
    const fullUrl = `${this.url}size=${size}&numDice=${numdice}&rolls=${rolls}`;
    return this.http.get<number[]>(fullUrl);
  }
}
