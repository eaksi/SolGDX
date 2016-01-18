package com.github.eaksi.stactics.db;

import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

public class Creature {

	private static AtomicInteger runningId = new AtomicInteger();

	private int id;

	private String name;
	private String prefixName, suffixName; // titles and descriptors

	// "natural", mostly unchanging stats
	private int naturalHP, naturalDelay;
	
	private int baseMovement;
	
	private int hp, hpMax;
	private int moves, movesMax;
	private int actions, actionsMax;
	
	private int baseDelay, delay;
	
	public Hashtable<Integer, Integer> skills; // XXX: temp public

	public Creature() {

		id = runningId.incrementAndGet(); // generates unique id
		skills = new Hashtable<Integer, Integer>();
		prefixName = "";
		name = World.getRandomFName();
		suffixName = World.getRandomLName();

		naturalHP = 4 + RNG.nextInt(2);
		hpMax = naturalHP;
		hp = naturalHP;
		
		baseMovement = 4;
		movesMax = baseMovement;
		moves = baseMovement;
		
		naturalDelay = 10 + RNG.nextInt(4);
		baseDelay = naturalDelay;
		delay = naturalDelay;
		
		actionsMax = 1;
		actions = actionsMax;
		
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

	public int getHP() {
		return hp;
	}
	
	public void applyDamage(int damage) {
		hp -= damage;
		if (hp < 0) hp = 0;
	}
	
	
	public int getMoves() {
		return moves;
	}

	public int getActions() {
		return actions;
	}
	
	public String getMAString() {
			return "(A" + actions + " M" + moves + ")";
	}
	
	public void spendAction() {
		actions--;
	}
	
	public void spendMove() {
		moves--;
	}
	
	public void startNewTurn() {
		actions = actionsMax;
		moves = movesMax;
	}
	
	// temp: quick and dirty method for displaying hp in symbols
	public String getStringHP(boolean dots) {
		if (dots) {
			String h = "";
			for (int i = 0; i < hpMax; i++) {
				if (i >= hp ) {
					h = h.concat("-");
				} else {
					h = h.concat("o");
				}
			}
			return h;
		} else {
			return hp + "/" + hpMax;
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

	public boolean isReady() {

		if (delay <= 0) {
			delay += baseDelay;
			System.out.println(getFullName() + ": ACTION");
			return true;
		}
		System.out.println(getFullName() + " (base:" + baseDelay + ") delay: " + delay);
		delay--;
		return false;
	}

}
