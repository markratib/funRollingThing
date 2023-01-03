package com.example.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.threads.CalculationThread;

@RestController
@RequestMapping(value="/roll")
public class RollController 
{

	@CrossOrigin
	@GetMapping
	public ResponseEntity rollDice(@RequestParam int size, @RequestParam int numDice, @RequestParam int rolls)
	{
		List<CalculationThread> threadList = new ArrayList<CalculationThread>();
		
		//Holds the result of rolling numDice
		List<Integer> results = new ArrayList<Integer>();
		
		//Check for bad inputs
		if(numDice < 1)
		{
			//If numDice is less than 1, set it to 1.
			//In the future, a maximum can also be set
			numDice = 1;
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
		}
//		if(rolls > 500000000)
//		{
//			rolls = 500000000;
//		}
		
		long beforeTime = System.currentTimeMillis();
//		results = calcRolls(size, numDice, rolls);
		results = calcRollsThreaded(size, numDice, rolls);
		
		
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
	
	public static List<Integer> calcRollsThreaded (int size, int numDice, int rolls)
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
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
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
}
