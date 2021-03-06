package com.github.eaksi.stactics.engine.gfx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.eaksi.stactics.db.Creature;
import com.github.eaksi.stactics.engine.Gfx;

/**
 *  Actors in a scene or a battle map. This class contains
 *  animation data and a link to a Creature.
 */
public class Actor extends Drawable {

	public enum Direction {
		NE, SE, SW, NW
	}

	public enum Animation {
		IDLE, WALK, KO, ATTACK, HURT
	}

	public enum AnimFrame {
		STAND, WALK1, WALK2, ATTACKING, DAMAGED
	}

	public Creature cr; // reference to creature

	private Animation currentAnimation;
	public int animFrameNr; // current animation frame
	private Direction heading;
	private AnimFrame animFrame;

	public Actor(Creature creat, int x, int y) {

		super();
		cr = creat;
		
		currentAnimation = Animation.IDLE;
		animFrameNr = -1;
		heading = Direction.NE;
		animFrame = AnimFrame.STAND;

		tileX = x;
		tileY = y;

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

	
	@Override
	public TextureRegion getSprite() {
		return Gfx.getEntitySprite(heading, animFrame);
	}


	/**
	 * Updates the animation on this Actor.
	 */
	public boolean updateAnimation() {
	
		switch(currentAnimation) {
		
		case IDLE:
			animFrameNr = -1;
			animFrame = AnimFrame.STAND;
			break; 
		case WALK:
			if (animFrameNr == -1 || animFrameNr >= 16) {
				animFrameNr = -1;
				animFrame = AnimFrame.STAND;
				currentAnimation = Animation.IDLE;
			} else {
				switch(animFrameNr) {
					case 0: case 1: case 7: case 8: case 9: case 15:
						animFrame = AnimFrame.STAND;
						moveForOneFrame();
						break;
					case 2: case 3: case 4: case 5: case 6:
						animFrame = AnimFrame.WALK1;
						moveForOneFrame();
						break;
			    	case 10: case 11: case 12: case 13: case 14:
			    		animFrame = AnimFrame.WALK2;
			    		moveForOneFrame();
			    		break;
			    	default:
			    		System.err.println("Error: Wrong values in updateAnimFrame! (refactored wrong way?)");
						animFrameNr = -1;  // prevent animation infinite loops
						animFrame = AnimFrame.STAND;  // prevent animation infinite loops
				}
			}
			break;
		case ATTACK:
			if (animFrameNr == -1 || animFrameNr >= 16) {
				animFrameNr = -1;
				animFrame = AnimFrame.STAND;
				currentAnimation = Animation.IDLE;
			} else {
				switch(animFrameNr) {
				case 0: case 1: case 2: case 3:
					animFrame = AnimFrame.WALK2;
					break;
				case 4: case 5: case 6: case 7: case 8: case 9:
					animFrame = AnimFrame.ATTACKING;
		    		break;
				case 10: case 11: case 12: case 13: case 14: case 15:
					animFrame = AnimFrame.STAND;
		    		break;
		    	default:
		    		System.err.println("Error: Wrong values in updateAnimFrame! (refactored wrong way?)");
					animFrameNr = -1;  // prevent animation infinite loops
					animFrame = AnimFrame.STAND;  // prevent animation infinite loops
				}
			}
			break;
		case HURT:
			if (animFrameNr == -1 || animFrameNr >= 16) {
				animFrameNr = -1;
				animFrame = AnimFrame.STAND;
				currentAnimation = Animation.IDLE;
			} else {
				animFrame = AnimFrame.DAMAGED;	// only one frame of animation

				// sprite moves from side to side when entity hit
				switch(animFrameNr) {
				case 0: case 1: case 2: case 3:
					isoX += 1;
					break;
				case 4: case 5: case 6: case 7:
					isoX -= 2;
					break;
				case 8: case 9:	case 10: case 11:
					isoX += 2;
					break;
				case 12: case 13: case 14: case 15:
					isoX -= 1;
					break;
		    	default:
		    		System.err.println("Error: Wrong values in updateAnimFrame! (refactored wrong way?)");
					animFrameNr = -1;  // prevent animation infinite loops
					animFrame = AnimFrame.STAND;  // prevent animation infinite loops
			}
		}
			break;
		default:
			System.err.println("Error: Invalid Animation on " + cr.getId() + ":" + cr.getName());
			animFrameNr = -1;  // prevent animation infinite loops
			animFrame = AnimFrame.STAND;  // prevent animation infinite loops
			break;
		}
		
		if (animFrameNr == -1) {
			return false;
		} else {
			animFrameNr++;
			return true;
		}
	}
	
	/**
	 * Moves the Actor in the right direction for 1 animation frame.
	 */
	private void moveForOneFrame() {

		//XXX: Y = Z for now, until height differences etc. are handled better.
				
		int modX = 0;
		int modY = 0;
		int modZ = 0;
		

		switch (heading) {
		case NE:
			modX = 2;
			modY = 1;
			modZ = 1;
			break;
		case SE:
			modX = 2;
			modY = -1;
			modZ = -1;
			break;
		case SW:
			modX = -2;
			modY = -1;
			modZ = -1;
			break;
		case NW:
			modX = -2;
			modY = 1;
			modZ = 1;
			break;
		default:
			break;
		}

		isoX += modX;
    	isoY += modY;
    	z += modZ;
	}
		
} // end class
