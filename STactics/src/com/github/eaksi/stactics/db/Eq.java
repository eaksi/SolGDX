package com.github.eaksi.stactics.db;

// Class for items and equipment
public class Eq {

	public enum Type {		// TODO: make more item types after the framework works
		SWORD,		// melee weapon
		BOW,		// ranged weapon
		ARMOR,		// armor
		EQUIPMENT,	// misc. equipment

		POTION,		// single use, usable, stackable			//XXX: own class for consumables/stackables?
		BOMB,		// single use, aimable weapon, stackable	//XXX: own class for consumables/stackables?
		SCROLL,  	// single(?) use, castable, stackable		//XXX: own class for consumables/stackables?
		RECIPE		// a recipe book for (possible) crafting	//XXX: own class for recipes and quest items?
	}
	
	private boolean customized = false;		// true if the item has changed permanently, cannot set back to false (sorting etc.)
	private boolean tempChanged = false;	// true if item has changed temporarily,  can be set back to false
	private boolean unique = false;			// true if only one can exist within the game (mainly for artifacts and quest items)
	
	
}
