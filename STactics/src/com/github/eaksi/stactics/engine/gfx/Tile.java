package com.github.eaksi.stactics.engine.gfx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.eaksi.stactics.engine.Gfx;

/**
 * Tiles are (almost always) immovable and persistent floor tiles in AreaMap.
 */
public class Tile extends Drawable {
	
	private boolean water;		// is the tile water or not
	private int height;			// for height data (may be redundant)
	
	public Tile(int tX, int tY, int iX, int iY, int drawDepth, boolean w, int height) {
		
		super();
		tileX = tX;
		tileY = tY;
		
		isoX = iX;
		isoY = iY;
		
		z = drawDepth;
		
		water = w;
		this.height = height;
		
	}
	
	public boolean isWater() {
		return this.water;
	}
	
	public int getHeight() {
		return height;
	}

	@Override
	public TextureRegion getSprite() {
		return Gfx.getTileSprite(water);
	}
	
}
