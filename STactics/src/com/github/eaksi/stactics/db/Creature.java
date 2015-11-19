package com.github.eaksi.stactics.db;

import java.util.Hashtable;
import java.util.Vector;

public class Creature {
	
	private int id;
	
	private String name;
	private String prefixName, suffixName; // titles and descriptors
	
	// base stats
	private int baseHP, baseDelay;
	
	// x, y on battlemap
	private int delay;
	
	private Vector<Integer> knownSpells;
	public Hashtable<Integer, Integer> skills; // XXX: temp public
	
	public Creature() {
		
		skills = new Hashtable<Integer,Integer>();
		id = 1;
		prefixName = "";
		name = World.getRandomFName();
		suffixName = World.getRandomLName();
	
		baseHP = 10;

		baseDelay = 10;
		
		skills.put(1,2);
		
		// starting current stats
		delay = 10;
		
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
			System.out.println(this.getFullName() + ": action");
			return true;
		}
		System.out.println(this.getFullName() + ": delay: " + delay);
		delay--;
		return false;
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

	public String getName() {
		return name;
	}
	
	
	//public int getJumpHeight() // TODO: needs creature cclass id reference
	//public int getMove()       // TODO: needs creature cclass id reference
	//public int getSwim()       // TODO: needs creature cclass/skill id reference

	
}
