package com.github.eaksi.stactics.db;

/**
 *	Contains info on the battle map, height differences etc. 
 */
public class AreaMap {

	private int[][] terrainMap;		// make 3d eventually for terrain effects and good borders
	//public int[][] heightMap;

	public AreaMap() {
		terrainMap = new int[][]
		{{0,0,0,0,0,0,0,0,0,0,0},
		{0,1,1,1,1,1,1,1,1,1,0},
		{0,1,1,1,1,1,1,1,1,1,0},
		{0,1,1,1,1,1,1,1,1,1,0},
		{0,1,1,1,1,1,1,1,1,1,0},
		{0,1,1,1,1,1,1,1,1,1,0},
		{0,1,1,1,1,1,1,1,1,1,0},
		{0,1,1,1,1,1,1,1,1,1,0},
		{0,1,1,1,1,1,1,1,1,1,0},
		{0,1,1,1,1,1,1,1,1,1,0},
		{0,1,1,1,1,1,1,1,1,1,0},
		{0,1,1,1,1,1,1,1,1,1,0},
		{0,1,1,1,1,1,1,1,1,1,0},
		{0,1,1,1,1,1,1,1,1,1,0},
		{0,0,0,0,0,0,0,0,0,0,0}};
		/*
		// Huge map testing
		terrainMap = new int[][]
		{{1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,0},
		{1,0,0,0,1,1,1,1,0,0,1,1,1,0,1,1},
		{1,2,1,1,1,1,1,1,1,0,1,0,0,1,1,1},
		{1,1,1,2,1,1,0,1,1,2,1,0,2,1,1,1},
		{3,1,1,1,1,1,0,1,1,0,1,0,0,1,1,1},
		{1,1,0,1,1,1,0,1,1,1,0,0,0,1,1,1},
		{1,0,0,1,1,1,1,1,0,1,1,0,0,1,1,1},
		{1,0,0,0,1,1,1,0,1,1,1,0,0,1,1,1},
		{1,0,0,0,1,1,1,0,1,1,1,0,0,1,1,1},
		{1,0,0,1,1,1,1,1,0,0,1,1,1,1,1,0},
		{1,0,1,1,0,0,1,1,0,1,1,0,0,1,1,1},
		{1,0,0,1,1,1,1,1,0,1,1,0,0,1,1,1},
		{1,0,0,1,1,1,1,1,0,0,1,1,1,1,1,0},
		{1,0,0,0,1,1,0,1,0,0,1,1,1,1,1,0},
		{1,0,0,0,1,1,0,1,0,0,1,1,1,1,1,0},
		{1,1,1,1,1,0,1,0,0,1,1,1,1,1,0} };
		*/

	}
	
	public int[][] getMap() {
		return terrainMap;
	}
	
	public int getWidth() {
		return terrainMap.length;
	}
	
	public int getHeight() {
		return terrainMap[0].length;	// XXX: arrays should always be rectangular, test elsewhere
	}
	
	public int getTile(int x, int y) {
		if (x >= 0 && y >= 0 && x < this.getWidth() && y < this.getHeight()) {
			return terrainMap[x][y];
		} else {
			System.err.println("AreaMap Error: Tried to get coordinates out of bounds ("+x+","+y+")");
			return 0;
		}
	}
}
