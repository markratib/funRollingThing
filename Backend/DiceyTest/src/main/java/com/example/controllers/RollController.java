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

@RestController
@RequestMapping(value="/roll")
public class RollController 
{

	@CrossOrigin
	@GetMapping
	public ResponseEntity rollDice(@RequestParam int size, @RequestParam int numDice, @RequestParam int rolls)
	{
		//Random number generator.
		Random rng = new Random();
		int sum = 0;
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
}
