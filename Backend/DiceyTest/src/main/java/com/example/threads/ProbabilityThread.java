package com.example.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProbabilityThread extends Thread
{
	private Thread t;
	private String threadName;
	private int interval;
	private int diceSize;
	private int numDice;
	private int startNum;
	private int endNum;
	private List<Integer> results = new ArrayList<Integer>();
	
	public ProbabilityThread(String name, int interval, int diceSize, int numDice, int startNum, int endNum)
	{
		this.threadName = name;
		this.interval = interval;
		this.diceSize = diceSize;
		this.numDice = numDice;
		this.startNum = startNum;
		this.endNum = endNum;
	}
	
	public void run()
	{
		try
		{
			int temp = 0;
			boolean incNext = true;
			long startTime = System.currentTimeMillis();
			List<Integer> diceArray = new ArrayList<Integer>();

			System.out.println(threadName + " is running");
			//setup dice array
			for(int i = 0; i < numDice; i++)
			{
//				System.out.println("Adding dice # " + (i + 1));
				diceArray.add(1);
			}
			for(int i = startNum; i < endNum; i++)
			{
				for(int j = 0; j < numDice; j++)
				{
					temp += diceArray.get(j);
				}
//				System.out.println("Adding " + temp + " to results");
				results.add(temp);
				temp = 0;
				
				incNext = true;
				
				for(int j = 0; j < numDice && incNext; j++)
				{
					if(diceArray.get(j) < diceSize && incNext)
					{
						diceArray.set(j, diceArray.get(j) + 1);
						incNext = false;
					}else if(incNext)
					{
						diceArray.set(j, 1);
						incNext = true;
					}
				}
				
				for(int j = 0; j < numDice; j++)
				{
					System.out.println("Dice #" + (j + 1) + " = " + diceArray.get(j));
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
