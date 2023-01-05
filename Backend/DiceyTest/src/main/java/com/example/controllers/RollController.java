package com.example.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.threads.CalculationThread;
import com.example.threads.CalculationThread2;

@RestController
@RequestMapping(value="/roll")
public class RollController 
{

	@CrossOrigin
	@GetMapping
	public ResponseEntity rollDice(HttpServletResponse response, @RequestParam int size, @RequestParam int numDice, @RequestParam int rolls)
	{
		System.out.println("Receieved request with: \nDieSize: " + size + "\nNumDice: " + numDice + "\nRolls: " + rolls);
		List<CalculationThread> threadList = new ArrayList<CalculationThread>();
		response.setHeader("Transfer-Encoding", "chunked");
		//Holds the result of rolling numDice
		List<Integer> results = new ArrayList<Integer>();
		
		//Check for bad inputs
		if(numDice < 1)
		{
			//If numDice is less than 1, set it to 1.
			//In the future, a maximum can also be set
			numDice = 1;
		}else if(numDice > 10000)
		{
			numDice = 10000;
		}
		if(size < 1)
		{
			//If size is less than 1, set it to 1. A one sided die is silly.
			size = 2;
		}
		if(rolls < 1)
		{
			//If we roll less than 1 times, we don't roll at all. That's silly.
			rolls = 1;
		}else if(rolls > 450000000)
		{
			rolls = 450000000;
		}
		
		
		long beforeTime = System.currentTimeMillis();
		Date startDate = new Date(beforeTime);
		System.out.println("Request started at: " + startDate);
//		results = calcRolls(size, numDice, rolls);
//		results = calcRollsThreaded(size, numDice, rolls);
		results = calcRollsThreaded2(size, numDice, rolls);
		
		
		System.out.println("results size = " + results.size());
		System.out.println("Request took " + (System.currentTimeMillis() - beforeTime) + "ms");
//		System.out.println("results = " + results);
		return ResponseEntity.status(200).body(results);
		//The below was for testing stuff. Because I'm kind of dumb :)
//		List<Integer> intList = new ArrayList<Integer>();
//		
//		intList.add(size);
//		intList.add(numDice);
//		intList.add(rolls);
//		
//		return ResponseEntity.status(200).body(intList);
	}
	
	public static List<Integer> calcRolls(int size, int numDice, int rolls)
	{
		int sum = 0;
		//Random number generator.
		Random rng = new Random();
		List<Integer> results = new ArrayList<Integer>();
		//Loop for the number of desired rolls.
		for(int i=0; i < rolls; i++)
		{
			//Loop for the number of dice we want to roll.
			 for(int j = 0; j < numDice; j++)
			 {
				 // add the die we just rolled to the total sum of dice rolled.
				 sum += rng.nextInt(size) + 1;
			 }
			 //Add the sum of our rolled dice to the return array.
			 results.add(sum);
			 //reset sum.
			 sum = 0;
		}
		
		return results;
	}
	
	public static List<Integer> calcRollsThreaded(int size, int numDice, int rolls)
	{
		final int NUMTHREADS = 4;
		//Holds the result of rolling numDice
		List<Integer> results = new ArrayList<Integer>();
		List<CalculationThread> threadList = new ArrayList<CalculationThread>();
		//initialize thread list
		for(int i = 1; i <= NUMTHREADS; i++)
		{
			String threadName = "Thread-" + i;
			CalculationThread newThread = new CalculationThread(threadName, i, size, numDice, rolls, NUMTHREADS);
			threadList.add(newThread);
		}
		//start the threads
		for(CalculationThread thread: threadList)
		{
			thread.start();
		}
		boolean wait = true;
		int aliveThreads = NUMTHREADS;
		
		while(wait)
		{
			//check if the threads are finished
			for(CalculationThread thread: threadList)
			{
				//check if this thread is not working
				if(thread.isInterrupted())
				{
					aliveThreads--;
				}
				else //thread is dead
				{
					
				}
//						System.out.println("aliveThreads = " + aliveThreads);
			}
			System.out.println("aliveThreads = " + aliveThreads);
			//if no threads are active
			if(aliveThreads == 0)
			{
				//we dont need to wait and can exit this loop
				wait = false;
			}
			else//sleep for 1 second
			{
				try 
				{
					Thread.sleep(1000);
				}catch (InterruptedException e) {
					System.out.println("Something went wrong while waiting...");
					e.printStackTrace();
				}
			}
			aliveThreads = NUMTHREADS;
			System.out.println("Waiting...");
		}
		//merge the results together
		for(CalculationThread thread: threadList)
		{
//					System.out.println("adding " + thread.getResults() + " from " + thread.getName());
			results.addAll(thread.getResults());
			thread.freeResults();
		}
		
		return results;
	}
	
	public static List<Integer> calcRollsThreaded2(int size, int numDice, int rolls)
	{
		//max threads allowed
		final int MAXTHREADS = 16;
		//number of threads we can use
		int numThreads = 1;
		List<Integer> results = new ArrayList<Integer>();
		
		//calc the max threads we should have
		for(int i = 2; i <= MAXTHREADS; i++)
		{
			//check if the number of rolls is divisible by i
			if(rolls % i == 0)
			{
				//if it is divisible, set numThreads to i
				numThreads = i;
			}
		}
		//check if numThreads is 1
		if(numThreads == 1)
		{
			//call the calculation here, it will be faster because of some of the checks in the
			//threaded version.
			results = calcRolls(size, numDice, rolls);
		}else//we have multiple possible threads, do the calculation with threading
		{
			int numRolls = rolls / numThreads;
			List<CalculationThread2> threadList = new ArrayList<CalculationThread2>();
			//populate thread list
			for(int i = 0; i < numThreads; i++)
			{
				String threadName = "Thread-" + i;
				CalculationThread2 newThread = new CalculationThread2(threadName, i, size, numDice, rolls, numRolls*i, numRolls*(i+1));
				threadList.add(newThread);
			}
			//start the threads
			for(CalculationThread2 thread: threadList)
			{
				thread.start();
			}
			
			boolean wait = true;
			int aliveThreads = numThreads;
			//wait for the threads to finish
			while(wait)
			{
				//check if the threads are finished
				for(CalculationThread2 thread: threadList)
				{
					//check if this thread is not working
					if(thread.isInterrupted())
					{
						aliveThreads--;
					}
					else //thread is dead
					{
						
					}
//							System.out.println("aliveThreads = " + aliveThreads);
				}
				System.out.println("aliveThreads = " + aliveThreads);
				//if no threads are active
				if(aliveThreads == 0)
				{
					//we dont need to wait and can exit this loop
					wait = false;
				}
				else//sleep for 1 second
				{
					try 
					{
						Thread.sleep(1000);
					} catch (InterruptedException e) 
					{
						System.out.println("Something went wrong while waiting...");
						e.printStackTrace();
					}
				}
				aliveThreads = numThreads;
				System.out.println("Waiting...");
			}
			//populate the return list
			for(CalculationThread2 thread: threadList)
			{
				//System.out.println("adding " + thread.getResults() + " from " + thread.getName());
				results.addAll(thread.getResults());
				thread.freeResults();
			}
		}
		
		System.out.println("Number of threads: " + numThreads);
		return results;
	}
}
