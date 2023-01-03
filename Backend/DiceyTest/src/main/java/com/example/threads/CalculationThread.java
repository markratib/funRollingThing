package com.example.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CalculationThread extends Thread
{
	private Thread t;
	private String threadName;
	private int interval;
	private int diceSize;
	private int numDice;
	private int numRolls;
	private List<Integer> results = new ArrayList<Integer>();
	
	private int maxThreads;
	public CalculationThread(String name, int interval, int diceSize, int numDice, int numRolls, int maxThreads)
	{
		this.threadName = name;
		this.interval = interval;
		this.diceSize = diceSize;
		this.numDice = numDice;
		this.numRolls = numRolls;
		
		this.maxThreads = maxThreads;
	}
	
	public void run()
	{
		try
		{
			long startTime = System.currentTimeMillis();
			System.out.println(threadName + " is running");
			//initialize the counter variable for each thread
			int i = 1 * interval;
			int sum = 0;
			boolean doCalc = true;
			
			Random rng = new Random();
			if(interval != 1)
			{
				//begin calculation
				for( ; i <= numRolls; i += interval)
				{
					doCalc = true;
					//check if the current number is divisible by a higher thread interval
					for(int j = interval + 1; j <= maxThreads; j++)
					{
//						System.out.println(this.threadName + " checking " + i + " for calculation.");
						//check for divisibility
						if((i % j) == 0)
						{
//							System.out.println(this.threadName + " is not calculating " + i);
							doCalc = false;
						}
					}
					
					if(doCalc)
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
					
				}
			}else//interval is 1
			{
				for( ; i <= numRolls; i++)
				{
					doCalc = true;
					//check if the current number is divisible by a higher thread interval
					for(int j = interval + 1; j < maxThreads; j++)
					{
						//check for divisibility
						if(i % j == 0)
						{
							doCalc = false;
						}
					}
					
					if(doCalc)
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
					
				}
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
