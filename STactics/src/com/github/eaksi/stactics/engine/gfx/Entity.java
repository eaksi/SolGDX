package com.github.eaksi.stactics.engine.gfx;

import com.github.eaksi.stactics.db.Creature;

// Entities are battlers in the combat engine, contain animation data and a link to db Creature
public class Entity extends Drawable {
	
	public enum Direction {
		NE, SE, SW, NW
	}
	
	public enum Animation {
		IDLE, STAND, WALK, KO, ATTACK, HIT
	}
	
	public enum SpriteType {
		NE_STAND, SE_STAND, SW_STAND, NW_STAND,
		NE_WALK1, SE_WALK1, SW_WALK1, NW_WALK1,
		NE_WALK2, SE_WALK2, SW_WALK2, NW_WALK2
	}
	

	public Creature cr;		// reference to creature
	//private int spriteId;

	private Animation currentAnimation;
	public int animFrame;				// frames left in animation
	private Direction heading;
	
	private int delay;
	
	
	
	public Entity(Creature creat) {
		
		super();
		cr = creat;
		
		currentAnimation = Animation.IDLE;
		animFrame = -1;
		heading = Direction.NE;
		
		tileX = 0;
		tileY = 0;
		
	}

	public Animation getAnimation() {
		return currentAnimation;
	}

	public void setAnimation(Animation currentAnimation) {
		this.currentAnimation = currentAnimation;
	}
	
	public Direction getHeading() {
		return heading;
	}
	
	public void setHeading(Direction dir) {
		this.heading = dir;
	}
	
	public int getFramesLeft() {
		return animFrame;
	}

	public String getAnimString() {
		return currentAnimation.toString();
	}

	
	// getters and setters
	/*public int getIsoX() {return isoX;}
	public void setIsoX(int isoX) {this.isoX = isoX;}
	public int getIsoY() {return isoY;}
	public void setIsoY(int isoY) {this.isoY = isoY;}
	*/
}
