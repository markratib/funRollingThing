import { Component, OnInit } from '@angular/core';
import { DiceServiceService } from 'src/app/services/dice-service.service';
import { LinkingServiceService } from 'src/app/services/linking-service.service';
import { transferObject } from 'src/app/tansferObject';

@Component({
  selector: 'app-roll-input',
  templateUrl: './roll-input.component.html',
  styleUrls: ['./roll-input.component.css']
})
export class RollInputComponent implements OnInit {
  baseUrl: string = "http://127.0.0.1:9000/roll?";
  diceSize: number = 6;
  numDice: number = 2;
  diceRolls: number = 20;
  rollResults?: number[];
  tObject?: transferObject;
  constructor(private diceService: DiceServiceService, private linkService: LinkingServiceService) { }

  ngOnInit(): void {
    this.diceService.url = this.baseUrl;
  }
  //This is based off what the chatbot told me to do. I stopped using it.
  rollDice()
  {
    try
    {
      const result = this.diceService.rollDice(this.diceSize, this.numDice, this.diceRolls);
      
      console.log(result);
    } catch (err)
    {
      console.log(err);
    }
  }

  rollDice2()
  {
    try
    {
      const result = this.diceService.rollDice2(
        this.diceSize, 
        this.numDice, 
        this.diceRolls).subscribe(data => {
          console.log(data);
          this.rollResults = data;
          this.tObject = new transferObject(data, this.diceSize, this.numDice, this.diceRolls)
          console.log(this.tObject)
          this.linkService.addResults(this.tObject);
        });
    } catch (err)
    {
      console.log(err);
    }
  }

}
