package com.github.eaksi.stactics.db;

import java.util.Random;

/**
 * A very basic Random Number Generator class. Will be expanded later. 
 *
 */
public class RNG {

	private static Random rng;
	
	public static void initialize() {
		rng = new Random();
	}
	
	public static int nextInt(int bound) {
		return rng.nextInt(bound);
	}
	
	
	
}
