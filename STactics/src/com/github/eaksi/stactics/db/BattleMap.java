package com.github.eaksi.stactics.db;

public class BattleMap {

	public int[][] terrainMap;		// make 3d eventually for terrain effects and good borders
	//public int[][] heightMap;
	
	public BattleMap() {
		terrainMap = new int[][]   {{1,1,1,1,1,1,1},
									{1,0,0,0,1,1,1},
									{1,0,0,1,1,1,1},
									{1,1,1,1,1,1,0},
									{1,0,0,1,1,1,0},
									{1,0,0,1,1,1,0},
									{1,0,0,1,1,1,0},
									{1,0,0,0,1,1,0},
									{1,0,0,0,1,1,0},
									{1,0,0,0,1,1,0},
									{1,1,1,1,1,1,0} };
	}
	
	public int[][] getMap() {
		return terrainMap;
	}
	
	public int getWidth() {
		return terrainMap.length;
	}
	
	public int getHeight() {
		return terrainMap[0].length;	// XXX: arrays always rectangular, checked elsewhere
	}
	
	public int getTile(int x, int y) {
		if (x >= 0 && y >= 0 && x < this.getWidth() && y < this.getHeight()) {
			return terrainMap[x][y];
		} else {
			System.err.println("BattleMap Error: Tried to get coordinates out of bounds ("+x+","+y+")");
			return 0;
		}
	}
}