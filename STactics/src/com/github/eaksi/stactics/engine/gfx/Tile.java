package com.github.eaksi.stactics.engine.gfx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.eaksi.stactics.engine.Gfx;

/**
 * Tiles are (almost always) immovable and persistent floor tiles in AreaMap.
 */
public class Tile extends Drawable {
	
	private boolean water;
	
	public Tile(int tX, int tY, int iX, int iY, int depth, boolean w) {
		
		super();
		this.tileX = tX;
		this.tileY = tY;
		
		this.isoX = iX;
		this.isoY = iY;
		
		this.z = depth;
		
		this.water = w;
		
	}
	
	public boolean isWater() {
		return this.water;
	}

	@Override
	public TextureRegion getSprite() {
		return Gfx.getTileSprite(water);
	}
	
}
