package com.github.eaksi.stactics.engine.gfx;

public class Tile extends Drawable {
	
	private boolean water;
	
	public Tile(int x, int y, int depth, boolean w) {
		this.isoX = x;
		this.isoY = y;
		this.z = depth;
		this.water = w;
		
	}
	
	public boolean isWater() {
		return this.water;
	}
	
}
