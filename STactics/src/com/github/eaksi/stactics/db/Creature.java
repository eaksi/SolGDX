package com.github.eaksi.stactics.db;

import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Creature {

	private static AtomicInteger runningId = new AtomicInteger();

	private int id;

	private String name;
	private String prefixName, suffixName; // titles and descriptors

	// base stats
	private int baseHP, baseDelay;
	
	private int hp, delay;

	public Hashtable<Integer, Integer> skills; // XXX: temp public

	public Creature() {

		id = runningId.incrementAndGet(); // generates unique id
		skills = new Hashtable<Integer, Integer>();
		prefixName = "";
		name = World.getRandomFName();
		suffixName = World.getRandomLName();

		baseHP = 4 + World.rng.nextInt(2);
		hp = baseHP - 2; //World.rng.nextInt(2);

		baseDelay = 10 + World.rng.nextInt(4);

		skills.put(1, 2);

		// starting current stats
		delay = baseDelay + World.rng.nextInt(5);

	}

	public Creature(String pre, String nam, String suf) {

		this();
		prefixName = pre;
		name = nam;
		suffixName = suf;

	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getBaseDelay() {
		return baseDelay;
	}
	
	public int getHP() {
		return hp;
	}
	
	// temp: quick and dirty method for displaying hp in symbols
	public String getStringHP() {
		String h = "";
		for (int i = 0; i < baseHP; i++) {
			if (i >= hp ) {
				h = h.concat("-");
			} else {
				h = h.concat("o");
			}
		}
		return h;
	}
	
	public String getFullName() {
		String fullName = "";
		if (prefixName.length() > 0) {
			fullName = fullName.concat(prefixName + " ");
		}

		fullName = fullName.concat(name);

		if (suffixName.length() > 0) {
			fullName = fullName.concat(" " + suffixName);
		}

		return fullName;
	}


}
