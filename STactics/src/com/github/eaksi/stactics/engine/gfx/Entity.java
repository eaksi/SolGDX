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
		IDLE, WALK, KO, ATTACK, HIT
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

	public boolean updateAnimFrame() {
		/* if (animFrameNr == -1) {
			currentAnimation = Animation.IDLE;
		}
		*/		
		
		switch(currentAnimation) {
		
		case IDLE:
			animFrameNr = -1;
			animFrame = AnimFrame.STAND;
			break;
		
		case WALK:
			if (animFrameNr == -1 || animFrameNr >= 16) {
				animFrameNr = -1;	// TODO: more elegant solution
				animFrame = AnimFrame.STAND;
				currentAnimation = Animation.IDLE;
				break;	// XXX: hmm
			} else {
				switch(animFrameNr) {
					case 0: case 1: case 7: case 8: case 9: case 15:
						animFrame = AnimFrame.STAND;
						break;
					case 2: case 3: case 4: case 5: case 6:
						animFrame = AnimFrame.WALK1;
						break;
			    	case 10: case 11: case 12: case 13: case 14:
			    		animFrame = AnimFrame.WALK2;
			    		break;
			    	default:
			    		System.err.println("Error: Wrong values in updateAnimFrame! (refactored wrong way?)");
						animFrameNr = -1;  // prevent animation loops
						animFrame = AnimFrame.STAND;  // prevent animation loops
				}
				
				animFrameNr++;
			
			}
			break;
			
		default:
			System.err.println("Error: Invalid Animation on " + cr.getId() + ":" + cr.getName());
			animFrameNr = -1;  // prevent animation loops
			animFrame = AnimFrame.STAND;  // prevent animation loops
			break;
			
		}
		
		if (animFrameNr == -1) {
			return false;
		} else {
			return true;
		}
	}
		
} // end class
