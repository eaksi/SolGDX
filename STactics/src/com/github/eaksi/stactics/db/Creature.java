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

	// x, y on battlemap
	private int delay; // XXX: move to Entity?

	private Vector<Integer> knownSpells;
	public Hashtable<Integer, Integer> skills; // XXX: temp public

	public Creature() {

		id = runningId.incrementAndGet(); // generates unique id
		skills = new Hashtable<Integer, Integer>();
		prefixName = "";
		name = World.getRandomFName();
		suffixName = World.getRandomLName();

		baseHP = 5;

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

	public boolean isReady() {

		if (delay <= 0) {
			delay += baseDelay;
			System.out.println(this.getFullName() + ": ACTION");
			return true;
		}
		System.out.println(this.getFullName() + " (base:" + baseDelay + ") delay: " + delay);
		delay--;
		return false;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
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

	// public int getJumpHeight() // TODO: needs creature cclass id reference
	// public int getMove() // TODO: needs creature cclass id reference
	// public int getSwim() // TODO: needs creature cclass/skill id reference

}
