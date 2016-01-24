package com.github.eaksi.stactics.db;

public class Skill {

	// XXX: think of a better implementation 
	
	private int id;					// skill id (not generated here)				
	private String name;			// skill name
	private int min, max;			// minimum and maximum skill level
	//private int skillType; 		// mainly for GUI purposes, is the skill level 1-8 or normal/veteran/elite or on/off etc.
	//private int skillCategory;	// weapons, etc.
	
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
