package com.github.eaksi.stactics.db;

public class BattleMap {

	public int[][] terrainMap;		// make 3d eventually for terrain effects and good borders
	
	public BattleMap() {
		terrainMap = new int[][]   {{1,1,1,1,1,1},
									{1,0,0,0,1,1},
									{1,0,0,1,1,1},
									{1,1,1,1,1,1},
									{1,0,0,1,1,1},
									{1,0,0,1,1,1},
									{1,0,0,1,1,1},
									{1,0,0,0,1,1},
									{1,0,0,0,1,1},
									{1,0,0,0,1,1},
									{1,1,1,1,1,1} };
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
}
