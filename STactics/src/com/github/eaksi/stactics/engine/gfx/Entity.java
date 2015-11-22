package com.github.eaksi.stactics.engine.gfx;

import com.github.eaksi.stactics.db.Creature;

// Entities are battlers in the combat engine, contain animation data and a link to db Creature
public class Entity {
	
	public enum Direction {
		NE, SE, SW, NW
	}
	
	public enum Animation {
		IDLE, STAND, STILL, KO, ATTACK, HIT
	}
	
	public enum AnimFrame {
		NE_STAND, SE_STAND, SW_STAND, NW_STAND,
		NE_WALK1, SE_WALK1, SW_WALK1, NW_WALK1,
		NE_WALK2, SE_WALK2, SW_WALK2, NW_WALK2
	}
	
	private int id;			// id inside animation framework
	public Creature cr;		// reference to creature
	//private int spriteId;

	private Animation currentAnimation;
	private int framesLeft;				// frames left in animation
	private Direction heading;
	
	private int isoX, isoY;		// used by graphics engine
	private int delay;			
	
	public int tileX, tileY;	// used by AI, graphics engine
	
	
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
