package com.github.eaksi.stactics.db;

public class Skill {
	
	private int id;
	private String name;
	private int min, max;
	//private int skillType; // mainly for GUI purposes, is the skill level 1-8 or normal/veteran/elite or on/off etc.
	//private int skillCategory; // weapons, etc.
	
	public Skill(int id, String name, int min, int max) {
		this.name = name;
		this.min = min;
		this.max = max;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
	
}
