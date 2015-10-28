package com.github.eaksi.stactics.db;

import java.util.Hashtable;
import java.util.Vector;

public class Creature {
	
	private int id;
	
	private String name;
	private String prefixName, suffixName; // titles and descriptors

	//private int race, cclass, alignment;
	//private Vector<History> characterHistory;  //idea
	
	
	// base stats
	private int strength, agility, mind;
	private int baseHP, baseMP, baseTP, baseDelay;
	
	private Vector<Integer> knownSpells;
	public Hashtable<Integer, Integer> skills; // XXX: temp public
	
	public Creature() {
		
		skills = new Hashtable<Integer,Integer>();
		id = 1;
		prefixName = "";
		name = "Vahness";
		suffixName = "Cormaian";
	
		strength = 10;		// damage done
		agility = 10;		// 
		mind = 10;
				
		baseHP = 10;
		baseMP = 10;
		baseTP = 10;
		baseDelay = 50;
		
		skills.put(1,2);
		
	}
	
	
	// TODO: getters and setters
	// TODO: spell id list of integers
	
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
	
	//Derived stats
	/*public int getMeleeAttack() { return str*2 + dex + agil; }
	public int getRangedAttack() { return dex*2 + str + agil; }
	public int getMagicAttack() { return intl*3 + mind; }
	public int getPhysicalDefense() { return vit*2 + avd*2; }
	public int getMagicDefense() { return res*3 + mind; }*/
	
	//public int getJumpHeight() // TODO: needs creature cclass id reference
	//public int getMove()       // TODO: needs creature cclass id reference
	//public int getSwim()       // TODO: needs creature cclass/skill id reference
	
	
	
}
