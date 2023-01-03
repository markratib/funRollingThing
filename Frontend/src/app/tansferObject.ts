
export class transferObject
{
    rollResults?: number[];
    probResults?: number[];
    diceSize?: number;
    numDice?: number;
    diceRolls?: number;

    constructor(rollResults: number[], diceSize: number, numDice: number, diceRolls: number)
    {
        this.rollResults = rollResults;
        this.diceSize = diceSize;
        this.numDice = numDice;
        this.diceRolls = diceRolls;
    }
}