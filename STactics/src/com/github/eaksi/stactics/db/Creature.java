package com.github.eaksi.stactics.db;

import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

public class Creature {

	private static AtomicInteger runningId = new AtomicInteger();

	private int id;

	private String name;
	private String prefixName, suffixName; // titles and descriptors

	// base stats
	private int baseHP, baseDelay, baseMove;
	
	private int hp;

	public Hashtable<Integer, Integer> skills; // XXX: temp public

	public Creature() {

		id = runningId.incrementAndGet(); // generates unique id
		skills = new Hashtable<Integer, Integer>();
		prefixName = "";
		name = World.getRandomFName();
		suffixName = World.getRandomLName();

		baseHP = 4 + RNG.nextInt(2);
		hp = baseHP;
		
		baseMove = 4;

		baseDelay = 10 + RNG.nextInt(4);

		skills.put(1, 2);

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
	
	public int getBaseMove() {
		return baseMove;
	}
	public int getHP() {
		return hp;
	}
	
	public void applyDamage(int damage) {
		hp -= damage;
		if (hp < 0) hp = 0;
	}
	
	// temp: quick and dirty method for displaying hp in symbols
	public String getStringHP(boolean dots) {
		if (dots) {
			String h = "";
			for (int i = 0; i < baseHP; i++) {
				if (i >= hp ) {
					h = h.concat("-");
				} else {
					h = h.concat("o");
				}
			}
			return h;
		} else {
			return hp + "/" + baseHP;
		}
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
