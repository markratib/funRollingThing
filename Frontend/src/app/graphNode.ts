export class GraphNode
{
    //the number for the node on the graph. this repesents a number that can be rolled by the dice
    num: number = 0;
    //the number of times this result happens
    quantity: number = 0;

    constructor() { }

    setNum(int:number): void
    {
        this.num = int;
    }
    setQuantity(int: number): void
    {
        this.quantity = int;
    }
    getNum(): number
    {
        return this.num;
    }
    getQuantity(): number
    {
        return this.quantity;
    }

    toString(): string
    {
        return "num = " + this.num + " quantity = " + this.quantity;
    }
}