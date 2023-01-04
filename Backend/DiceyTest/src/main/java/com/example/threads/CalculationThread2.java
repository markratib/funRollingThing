package com.example.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CalculationThread2 extends Thread
{
	private Thread t;
	private String threadName;
	private int interval;
	private int diceSize;
	private int numDice;
	private int numRolls;
	private int startNum;
	private int endNum;
	private List<Integer> results = new ArrayList<Integer>();
	
	public CalculationThread2(String name, int interval, int diceSize, int numDice, int numRolls, int startNum, int endNum)
	{
		this.threadName = name;
		this.interval = interval;
		this.diceSize = diceSize;
		this.numDice = numDice;
		this.numRolls = numRolls;
		this.startNum = startNum;
		this.endNum = endNum;
	}
	
	public void run()
	{
		try
		{
			long startTime = System.currentTimeMillis();
			System.out.println(threadName + " is running");
			int sum = 0;
			Random rng = new Random();
			
			for(int i = startNum; i < endNum; i++)
			{
				 //Loop for the number of dice we want to roll.
				 for(int j = 0; j < numDice; j++)
				 {
					 // add the die we just rolled to the total sum of dice rolled.
					 sum += rng.nextInt(diceSize) + 1;
				 }
				 //Add the sum of our rolled dice to the return array.
				 results.add(sum);
				 //reset sum.
				 sum = 0;
			}
			
			System.out.println(threadName + " has finished after " + (System.currentTimeMillis() - startTime) + "ms");
			
			t.interrupt();
			
		}catch(Exception e)
		{
			System.out.println(threadName + " encountered an exception.\n");
			e.printStackTrace();
		}
		
		
	}
	
	public void start()
	{
		System.out.println(threadName + " is starting...");
		//check if the thread has not been started yet.
		if(t == null)
		{
			t = new Thread(this, this.threadName);
			t.start();
		}
	}
	
	public boolean isInterrupted()
	{
		return t.isInterrupted();
	}
	
	public List<Integer> getResults()
	{
		return this.results;
	}
	
	public void freeResults()
	{
		this.results = null;
	}
}
