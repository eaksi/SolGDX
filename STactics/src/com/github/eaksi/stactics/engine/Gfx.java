package com.github.eaksi.stactics.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.eaksi.stactics.engine.gfx.Entity;

public class Gfx {

	protected static Texture tempTile;			// temp test tile
	static Texture tempTile0;			// temp test water tile
	
	private static Texture spriteSheet;
	private static TextureRegion[][] splitSheet;		// sprite sheet divided into regions
	private static TextureRegion[][] flippedSheet;		// same as splitSheet, but regions individually flipped

	public static void initialize() {
		
		spriteSheet = new Texture("data/farcher_placeholder.png");
		splitSheet = TextureRegion.split(spriteSheet, 32, 64);
		flippedSheet = TextureRegion.split(spriteSheet, 32, 64);

		// flip all sprites within sheet 
		for (int i=0; i<2; i++) {
			for (int j=0; j<7; j++) {
				flippedSheet[i][j].flip(true, false);
			}
		}
		
		tempTile = new Texture("data/64px_tile_placeholder.png");
		tempTile0 = new Texture("data/64px_tile_placeholder_water.png");
	}
	
	public static TextureRegion drawEntitySprite(Entity.Direction dir, Entity.AnimFrame anim) {
		switch (dir) {
		

		default:
			System.err.println("Error: drawEntitySprite direction wrong");
			// fallthrough
		case NE:
			switch(anim) {
			case STAND:
				return flippedSheet[0][3];
			case WALK1:
				return flippedSheet[0][4];
			case WALK2:
				return flippedSheet[0][5];
			default:
				System.err.println("Error: drawEntitySprite unknown animation frame!");
				return new TextureRegion();
			}
		case SE:
			switch(anim) {
			case STAND:
				return splitSheet[0][0];
			case WALK1:
				return splitSheet[0][1];
			case WALK2:
				return splitSheet[0][2];
			default:
				System.err.println("Error: drawEntitySprite unknown animation frame!");
				return new TextureRegion();
			}
		case SW:
			switch(anim) {
			case STAND:
				return flippedSheet[0][0];
			case WALK1:
				return flippedSheet[0][1];
			case WALK2:
				return flippedSheet[0][2];
			default:
				System.err.println("Error: drawEntitySprite unknown animation frame!");
				return new TextureRegion();
			}
		case NW:
			switch(anim) {
			case STAND:
				return splitSheet[0][3];
			case WALK1:
				return splitSheet[0][4];
			case WALK2:
				return splitSheet[0][5];
			default:
				System.err.println("Error: drawEntitySprite unknown animation frame!");
				return new TextureRegion();
			} // end anim switch-case
		} // end dir switch-case
	} // method drawEntitySprite
	
	public static void dispose() {
		tempTile.dispose();
		tempTile0.dispose();
		spriteSheet.dispose();
	}
} // end class
