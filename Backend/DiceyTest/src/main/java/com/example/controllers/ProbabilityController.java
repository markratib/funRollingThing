package com.example.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.threads.ProbabilityThread;

@RestController
@RequestMapping(value="/probability")
public class ProbabilityController 
{
//	@CrossOrigin
//	@GetMapping
//	public ResponseEntity rollDice(@RequestParam int size, @RequestParam int numDice) throws IOException
//	{
//		List<Integer> results = new ArrayList<Integer>();
//		long startMilli = System.currentTimeMillis();
//		Date dateNow = new Date(System.currentTimeMillis());
//		System.out.println("Request recieved at: " + dateNow);
//		
//		results = determineProbability(size, numDice);
////		results = determineProbThreaded(response, size, numDice);
//		
//		System.out.println("Number of results: " + results.size());
//		System.out.println("Request finished in " + (System.currentTimeMillis() - startMilli) + "ms");
//		return ResponseEntity.status(200).body(results);
//	}
	
	@CrossOrigin
	@GetMapping
	public void rollDice(HttpServletResponse response, @RequestParam int size, @RequestParam int numDice) throws IOException
	{
		List<Integer> results = new ArrayList<Integer>();
		OutputStream outStream = response.getOutputStream();
		PrintWriter outWriter = new PrintWriter(outStream);
		long startMilli = System.currentTimeMillis();
		long resultsSize = 0;
		long startNum = 1;
		long interval = 100000;
		long endNum = interval;
		boolean doCalc = true;
		Date dateNow = new Date(System.currentTimeMillis());
		System.out.println("Request recieved at: " + dateNow);
//		response.setHeader("Transfer-Encoding", "chunked");
//		response.getWriter().println("hello :)");
//		response.flushBuffer();
		outWriter.println("[");
		//check if the end number is less than the actual end
		System.out.println(((int) Math.pow(size, numDice) < endNum));
		if((int) Math.pow(size, numDice) < endNum)
		{
			System.out.println("Number of possibilitie: " + Math.pow(size, numDice) +
					"\nSetting endNum to " + endNum);
			endNum = (int) Math.pow(size, numDice);
		}
		do
		{
			results = null;
			results = new ArrayList<Integer>();
			System.out.println("startNum: " + startNum + ". endNum: " + endNum);
			results = determineProbability(size, numDice, startNum, endNum);
			//response.getWriter().print(results);
			for(Integer result : results)
			{
				outWriter.print("\n " + result.toString() + ", ");
			}
			outWriter.flush();
			//check if we're at the end of the list
			if(endNum == (int) Math.pow(size, numDice))
			{
				//if we are, break our of the loop
				doCalc = false;
			}else if(endNum + interval > (int) Math.pow(size, numDice))
			{
				//we're past the list, so we need to go back to the end
				startNum = endNum + 1;
				endNum = (int) Math.pow(size, numDice);
			}else
			{
				//we're not past the end, increment both start and end
				startNum = endNum + 1;
				endNum += interval;
			}
			resultsSize += results.size();
//			System.out.println("Results size so far: " + resultsSize + "\nresults.size(): " + results.size());
//			System.out.println("Element 1 of the list is: " + results.get(1));
//			System.out.println("**************************************************");
			results.clear();
		}while(doCalc);
		outWriter.print("\n]");
		
//		results = determineProbThreaded(response, size, numDice);
		outWriter.close();
		outStream.close();
		System.out.println("Number of results: " + resultsSize);
		System.out.println("Request finished in " + (System.currentTimeMillis() - startMilli) + "ms");
		response.setStatus(200);
	}
	
	//Problem encountered: How do I set up the dice for each thread? This seems like a very 
	//tough problem to solve. I think I'm going to abandon this for now.
	public List<Integer> determineProbThreaded(HttpServletResponse response, int size, int numDice, int startNum, int endNum) 
	{
		List<Integer> results = new ArrayList<Integer>();
		List<Integer> diceArray = new ArrayList<Integer>();
		int minResult = numDice;
		int maxResult = numDice * size;
		int temp = 0;
		boolean incNext = true;
		//max threads allowed
		final int MAXTHREADS = 16;
		//number of threads we can use
		int numThreads = 1;
		
		//calc the max threads we should have
		for(int i = 2; i <= MAXTHREADS; i++)
		{
			//check if the number of rolls is divisible by i
			if(maxResult % i == 0)
			{
				//if it is divisible, set numThreads to i
				numThreads = i;
			}
		}

		if(numThreads == 1)
		{
			//call the calculation here, it will be faster because of some of the checks in the
			//threaded version.
			results = determineProbability(size, numDice);
		}else//we have multiple possible threads, do the calculation with threading
		{
			//divide the work load based on the number of threads
			int numCalcs = maxResult / numThreads;
			List<ProbabilityThread> threadList = new ArrayList<ProbabilityThread>();
			//create the threads
			for(int i = 0; i < numThreads; i++)
			{
				String threadName = "Thread-" + i;
				ProbabilityThread newThread = new ProbabilityThread(threadName, numCalcs, size, numDice, (numCalcs * i), (numCalcs * i + 1));
				threadList.add(newThread);
			}
			//start the threads
			for(ProbabilityThread thread: threadList)
			{
				System.out.println(thread.getName() + " starting at " + thread.getStartNum() + " and ending at " + thread.getEndNum());
				thread.start();
			}
			
			boolean wait = true;
			int aliveThreads = numThreads;
			//wait for the threads to finish
			while(wait)
			{
				//check if the threads are finished
				for(ProbabilityThread thread: threadList)
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
			for(ProbabilityThread thread: threadList)
			{
				//System.out.println("adding " + thread.getResults() + " from " + thread.getName());
				results.addAll(thread.getResults());
				thread.freeResults();
			}
		}
		
		return results;
	}

	public static List<Integer> determineProbability(int size, int numDice, long startNum, long endNum)
	{
		List<Integer> results = new ArrayList<Integer>();
		List<Integer> diceArray = new ArrayList<Integer>();
//		Date startTime = new Date(startMilli);
		long minResult = numDice;
		long maxResult = numDice * size;
		int temp = 0;
		boolean incNext = true;
		
		//setup dice array
		for(int i = 0; i < numDice; i++)
		{
//			System.out.println("Adding dice # " + (i + 1));
			diceArray.add(1);
		}
		results.clear();
//		System.out.println("results in determineProbability function before calculation: " + results.size());
		for(long i = startNum; i <= endNum; i++)
		{
			for(int j = 0; j < numDice; j++)
			{
				temp += diceArray.get(j);
			}
//			System.out.println("Adding " + temp + " to results");
			results.add(temp);
			temp = 0;
			
			incNext = true;
			
			for(int j = 0; j < numDice && incNext; j++)
			{
				if(diceArray.get(j) < size && incNext)
				{
					diceArray.set(j, diceArray.get(j) + 1);
					incNext = false;
				}else if(incNext)
				{
					diceArray.set(j, 1);
					incNext = true;
				}
			}
			
//			for(int j = 0; j < numDice; j++)
//			{
//				System.out.println("Dice #" + (j + 1) + " = " + diceArray.get(j));
//			}
		}
//		System.out.println("results in determineProbability function after calculation: " + results.size());
		return results;
	}
}
