package com.github.eaksi.stactics.engine.gfx;

import com.github.eaksi.stactics.db.Creature;

// XXX: CLASS NOT USED FOR ANYTHING YET
// Entities are battlers in the combat engine, contain animation data and a link to db Creature
public class Entity {
	
	public enum Direction {
		NE, SE, SW, NW
	}
	
	public enum Animation {
		IDLE, STAND, STILL, KO, ATTACK, HIT
	}
	
	private int id;			// id inside animation framework
	public Creature cr;	// reference to creature
	private int spriteId;

	private Animation currentAnimation;
	private int framesLeft;
	private Direction heading;
	
	private int isoX, isoY;
	private int delay;
	
	public int tileX, tileY;
	
	
	public Entity(Creature creat) {
		
		cr = creat;
		
		currentAnimation = Animation.IDLE;
		framesLeft = 16;
		heading = Direction.NE;
		
		tileX = 1;
		tileY = 0;
		
	}
		
	// getters and setters
	public int getIsoX() {return isoX;}
	public void setIsoX(int isoX) {this.isoX = isoX;}
	public int getIsoY() {return isoY;}
	public void setIsoY(int isoY) {this.isoY = isoY;}
	
}
