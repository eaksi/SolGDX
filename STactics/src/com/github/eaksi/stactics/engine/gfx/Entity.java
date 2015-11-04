package com.github.eaksi.stactics.engine.gfx;

// Entities are battlers in the combat engine, contain animation data and a link to db Creature
public class Entity {
	
	public enum Direction {
		NE, SE, SW, NW
	}
	
	public enum Animation {
		IDLE, STAND, STILL, KO, ATTACK, HIT
	}
	
	private int id;			// id inside animation framework
	private int creatureId;	// id of the creature in db //XXX: maybe just make this a Creature class reference
	private int spriteId;
	
	private Animation currentAnimation;
	private int framesLeft;
	private Direction heading;
	
	private int isoX, isoY;
	
	private int tileX, tileY;
	
	
	public Entity() {
		
		currentAnimation = Animation.IDLE;
		framesLeft = 16;
		heading = Direction.NE;
		
		tileX = 0;
		tileY = 0;
		
	}
	
	
}
