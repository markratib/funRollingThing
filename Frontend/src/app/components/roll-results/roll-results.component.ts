import { Component, Input, OnInit, HostListener, Host } from '@angular/core';
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
  numPossibilities?: number;
  showDetails: boolean = true;
  minResults: number = 0;
  maxResults: number = 0;
  
  graphNodes = new Array<GraphNode>;
  delayTime?: number;
  public options: any;
  data: Array<GraphNode> = new Array();
  scrnHeight:any;
  scrnWidth:any;

  //get screen width in real time and resize the chart as it happens
  @HostListener('window:resize', ['$event'])
  getScreenSize()
  {
    this.scrnHeight = window.innerHeight;
    this.scrnWidth = window.innerWidth;
    this.options = {
      width: this.scrnWidth,
    }
  }
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
    this.getScreenSize();
    // this.options = {
    //   title: {
    //     text: "Apple's revenue by product category",
    //   },
    //   subtitle: {
    //     text: 'in billion U.S. dollars',
    //   },
    //   series: [
    //     {
    //       type: 'column',
    //       xKey: 'quarter',
    //       yKey: 'iphone',
    //       yName: 'iPhone',
    //     },
    //     {
    //       type: 'column',
    //       xKey: 'quarter',
    //       yKey: 'mac',
    //       yName: 'Mac',
    //     },
    //     {
    //       type: 'column',
    //       xKey: 'quarter',
    //       yKey: 'ipad',
    //       yName: 'iPad',
    //     },
    //     {
    //       type: 'column',
    //       xKey: 'quarter',
    //       yKey: 'wearables',
    //       yName: 'Wearables',
    //     },
    //     {
    //       type: 'column',
    //       xKey: 'quarter',
    //       yKey: 'services',
    //       yName: 'Services',
    //     },
    //   ],
    // };
    
  }

  //Function to set up the chart
  chartSetup(): void
  {
    //bool to determine if it's worth it to calculate probability
    let calcProb: boolean = this.diceSize ** this.numDice < 100000000;
    let probability: Array<GraphNode> = new Array<GraphNode>();
    this.numPossibilities = this.diceSize ** this.numDice
    this.graphNodes = new Array<GraphNode>;
    //calculate the number of possible results
    this.minResults = this.numDice;
    this.maxResults = this.numDice * this.diceSize;
    if(calcProb)
    {
      probability = this.probabilitySetup();
    }
    //output to check stuff
    // console.log("minResults = " + this.minResults +
    //   "\nmaxResults = " + this.maxResults);
    //load up the data structure to be used in the graph
    for( let i = this.minResults; i <= this.maxResults; i++)
    {
      let newNode = new GraphNode();
      newNode.num = i;
      newNode.quantity = 0;
      if(calcProb)
      {
        newNode.probability = (this.diceRolls / this.diceSize ** this.numDice ) * probability[i - this.numDice].quantity;
      }
      this.graphNodes.push(newNode);
    }
    console.log(this.graphNodes)
    // for(let i = 0; i < porbability.length; i++)
    // {
    //   this.graphNodes[i].probability = porbability[i - this.numDice].quantity;
    //   // console.log(i);
    // }
    //output to check stuff
    // console.log("graphNodes = " + this.graphNodes);
    //put data into the nodes
    if(typeof this.rollResults != "undefined")
    {
      for(let i = 0; i < this.rollResults?.length; i++)
      {
        // console.log(this.rollResults[i]);
        this.graphNodes[this.rollResults[i] - this.minResults].quantity++;
      }
    }
    // console.log(this.graphNodes);
    this.data = this.graphNodes;
    this.options = {
      //says autsize is an unknown property
      // autosize: 'false',
      title: {text: "Roll Results"},
      height: this.diceSize * this.numDice * 40,
      width: this.scrnWidth,
      data: this.data,
      series: [{
        type: 'bar',
        xKey: 'num',
        yKey: 'quantity',
        yName: 'Times Rolled',
      },
      {
        type: 'bar',
        xKey: 'num',
        yKey: 'probability',
        yName: 'Probabilty'
      },],
    }
  }

  probabilitySetup(): Array<GraphNode>
  {
    let resultsCounter: Array<number> = new Array<number>;
    let diceCounter: Array<number> = [this.numDice];
    let diceArray: Array<number> = new Array<number>;
    let results: Array<number> = new Array<number>;
    let returnResults: Array<GraphNode> = new Array<GraphNode>;
    //set up the number of dice
    for(let i:number = 0; i<this.numDice; i++)
    {
      diceArray.push(1);
    }

    for(let i:number = 0; i < this.diceSize**this.numDice; i++)
    {
      let stuff: number = 0;
      for(let j: number = 0; j < this.numDice; j++)
      {
        stuff += diceArray[j];
      }
      results.push(stuff);

      let incNext: boolean = true;

      for(let j:number = 0; j < this.numDice; j++)
      {
        if(diceArray[j] < this.diceSize && incNext)
        {
          diceArray[j]++;
          incNext = false;
        }
        else if(incNext)
        {
          diceArray[j] = 1;
          incNext = true;
        }
      }
    }
    //add up each result
    // console.log("length = " + resultsCounter.length);
    for(let i:number = 0; i < this.numDice * this.diceSize - 1; i++)
    {
      resultsCounter.push(0);
    }
    for(let i:number = 0; i < results.length; i++)
    {
        resultsCounter[( results[i] - this.numDice )] += 1;
        // console.log("resultsCounter = " + resultsCounter.toString())
        // console.log("results[i] = " + (results[i] - this.numDice))
    }
    console.log("resultsCounter = " + resultsCounter.toString())
    //put it in the graphnode array
    for(let i:number = 0; i < resultsCounter.length; i++)
    {
      let newNode: GraphNode = new GraphNode;
      newNode.num = i + this.numDice;
      newNode.quantity = resultsCounter[i];

      returnResults.push(newNode);
    }

    return returnResults;
  }

  toggleDetails(): void
  {
    this.showDetails = !this.showDetails;
  }

  

  ngOnInit(): void {
    this.linkingService.getResults.subscribe(results => {
      // console.log("Results in rollresults component = " + results);
      this.rollResults = results.rollResults;
      this.chartSetup();
    })
  }
}
