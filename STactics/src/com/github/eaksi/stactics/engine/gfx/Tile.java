package com.github.eaksi.stactics.engine.gfx;

public class Tile extends Drawable {
	
	private boolean water;
	
	public Tile(int tX, int tY, int iX, int iY, int depth, boolean w) {
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
	
}
