package com.github.eaksi.stactics.engine.gfx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.eaksi.stactics.db.Creature;
import com.github.eaksi.stactics.engine.Gfx;

// Entities are battlers in the combat engine, contain animation data and a link to db Creature
public class Entity extends Drawable {

	public enum Direction {
		NE, SE, SW, NW
	}

	public enum Animation {
		IDLE, STAND, WALK, KO, ATTACK, HIT
	}

	public enum AnimFrame {
		STAND, WALK1, WALK2
	}

	public Creature cr; // reference to creature

	private Animation currentAnimation;
	public int animFrameNr; // current animation frame
	private Direction heading;
	private AnimFrame animFrame;
	
	private int baseDelay, delay;

	public Entity(Creature creat) {

		super();
		this.cr = creat;

		this.baseDelay = cr.getBaseDelay();
		
		currentAnimation = Animation.IDLE;
		animFrameNr = -1;
		heading = Direction.NE;
		animFrame = AnimFrame.STAND;

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

	public int getCurrentFrame() {
		return animFrameNr;
	}

	public String getAnimString() {
		return currentAnimation.toString();
	}
	
	public TextureRegion getSprite() {
		return Gfx.drawEntitySprite(heading, animFrame);
	}
	
	public boolean isReady() {

		if (delay <= 0) {
			delay += baseDelay;
			System.out.println(cr.getFullName() + ": ACTION");
			return true;
		}
		System.out.println(cr.getFullName() + " (base:" + baseDelay + ") delay: " + delay);
		delay--;
		return false;
	}

	public void updateAnimFrame() {
		/* if (animFrameNr == -1) {
			currentAnimation = Animation.IDLE;
		}
		*/		
		
		switch(currentAnimation) {
		
		case IDLE:
			animFrameNr = -1;
			break;
		case STAND:
			if (animFrameNr >= 16) {
dr5thdfrtht
				
			break;
		default:
			System.err.println("Error: Invalid Animation on " + cr.getId() + ":" + cr.getName());
			break;
			
				
			switch(animFrameNr) {
			case -1:
				animFrame = Animation.IDLE;
			}
		}
	}
		
		
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	// getters and setters
	/*
	 * public int getIsoX() {return isoX;} public void setIsoX(int isoX)
	 * {this.isoX = isoX;} public int getIsoY() {return isoY;} public void
	 * setIsoY(int isoY) {this.isoY = isoY;}
	 */
}
