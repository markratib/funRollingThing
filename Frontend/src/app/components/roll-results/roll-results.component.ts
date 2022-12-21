import { Component, Input, OnInit } from '@angular/core';
import { GraphNode } from 'src/app/graphNode';
import { LinkingServiceService } from 'src/app/services/linking-service.service';

@Component({
  selector: 'app-roll-results',
  templateUrl: './roll-results.component.html',
  styleUrls: ['./roll-results.component.css']
})
export class RollResultsComponent implements OnInit {
  @Input() rollResults?: number[];
  @Input() diceSize: number = 0;
  @Input() numDice: number = 0;
  @Input() diceRolls: number = 0;
  minResults: number = 0;
  maxResults: number = 0;
  graphNodes = new Array<GraphNode>;


  delayTime?: number;

  public options: any;

  // data = [
  //     {
  //         quarter: 'Q1',
  //         spending: 450,
  //     },
  //     {
  //         quarter: 'Q2',
  //         spending: 560,
  //     },
  //     {
  //         quarter: 'Q3',
  //         spending: 600,
  //     },
  //     {
  //         quarter: 'Q4',
  //         spending: 700,
  //     },
  // ];
  data: Array<GraphNode> = new Array();

  
  test(str:number, int:number): void
  {
    let newNode: GraphNode = new GraphNode();
    newNode.setNum(str);
    newNode.setQuantity(int);
    this.data.push(newNode);
    console.log("newNode = " + newNode)
    console.log(this.data);
  }

  constructor(private linkingService: LinkingServiceService) {

    //testing chart stuff
    // const stuff = { quarter:'Q5', spending: 1000}
    // this.data.push(stuff);
    this.test(1, 450);
    this.test(2, 500);
    this.test(3, 600);
    this.options = {
      data: this.data,
      series: [{
        type: 'column',
        xKey: 'num',
        yKey: 'quantity',
      }],
    };
  }

  chartSetup(): void
  {
    this.graphNodes = new Array<GraphNode>;
    //calculate the number of possible results
    this.minResults = this.numDice;
    this.maxResults = this.numDice * this.diceSize;
    console.log("minResults = " + this.minResults +
      "\nmaxResults = " + this.maxResults);
    
    for( let i = this.minResults; i <= this.maxResults; i++)
    {
      let newNode = new GraphNode();
      newNode.num = i;
      newNode.quantity = 0;
      this.graphNodes.push(newNode);
    }
    console.log("graphNodes = " + this.graphNodes);
    //put data into the nodes
    if(typeof this.rollResults != "undefined")
    {
      for(let i = 0; i < this.rollResults?.length; i++)
      {
        // console.log(this.rollResults[i]);
        this.graphNodes[this.rollResults[i] - this.minResults].quantity++;
        
      }
    }
    console.log(this.graphNodes);
    this.data = this.graphNodes;
    this.options = {
      series: [{
        data: this.data,
        type: 'bar',
        xKey: 'num',
        yKey: 'quantity',
      }],
    }
    
  }

  ngOnInit(): void {
    this.linkingService.getResults.subscribe(results => {
      console.log("Results in rollresults component = " + results);
      this.rollResults = results.rollResults;

      this.chartSetup();
    })
  }
}
