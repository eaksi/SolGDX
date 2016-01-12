package com.github.eaksi.stactics.db;

import java.util.Random;

public class RNG {

	private static Random rng;
	
	public static void initialize() {
		rng = new Random();
	}
	
	public static int nextInt(int bound) {
		return rng.nextInt(bound);
	}
	
	
	
}
