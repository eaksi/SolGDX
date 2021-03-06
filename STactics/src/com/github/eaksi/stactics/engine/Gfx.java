package com.github.eaksi.stactics.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.eaksi.stactics.engine.gfx.Actor;

/**
 * This class serves as a storage and contains help methods for drawing different graphics.
 */
public class Gfx {

	// tile graphics
	private static Texture landTile;
	private static Texture waterTile;
	private static TextureRegion landTileRegion;
	private static TextureRegion waterTileRegion;
	
	// character sprites
	private static Texture spriteSheet;
	private static TextureRegion[][] splitSheet;		// sprite sheet divided into regions
	private static TextureRegion[][] flippedSheet;		// same as splitSheet, but regions individually flipped

	// fonts
	private static BitmapFont font;			// main text font
	private static BitmapFont smallFont;	// secondary text font
	private static BitmapFont systemFont;	// system font

	public static void initialize() {
		
		// load sprite sheets
		spriteSheet = new Texture("data/farcher_placeholder.png");
		landTile = new Texture("data/tile_placeholder.png");
		waterTile = new Texture("data/tile_placeholder_water.png");

		splitSheet = TextureRegion.split(spriteSheet, 32, 64);		

		flippedSheet = TextureRegion.split(spriteSheet, 32, 64);

		// flip all sprites within sheet 
		for (int i=0; i<2; i++) {
			for (int j=0; j<7; j++) {
				flippedSheet[i][j].flip(true, false);
			}
		}

		// load tile graphics
		landTileRegion = new TextureRegion(landTile);
		waterTileRegion = new TextureRegion(waterTile);
		
		// load fonts
		font = FontLoader.loadFont(FontLoader.Type.MAIN);
		smallFont = FontLoader.loadFont(FontLoader.Type.SMALL);
		systemFont = FontLoader.loadSystemFont();
		
	}
	
	public static TextureRegion getTileSprite(boolean water) {
		if (water) {
			return waterTileRegion;
		} else {
			return landTileRegion;
		}
	}
	
	
	public static TextureRegion getEntitySprite(Actor.Direction dir, Actor.AnimFrame anim) {
		switch (dir) {

		default:
			System.err.println("Error: drawEntitySprite direction wrong");
			// fallthrough to case NE
		case NE:
			switch(anim) {
			case STAND:
				return flippedSheet[0][3];
			case WALK1:
				return flippedSheet[0][4];
			case WALK2:
				return flippedSheet[0][5];
			case ATTACKING:
				return flippedSheet[1][6];
			case DAMAGED:
				return flippedSheet[1][5];
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
			case ATTACKING:
				return splitSheet[0][6];
			case DAMAGED:
				return splitSheet[1][4];
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
			case ATTACKING:
				return flippedSheet[0][6];
			case DAMAGED:
				return flippedSheet[1][4];
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
			case ATTACKING:
				return splitSheet[1][6];
			case DAMAGED:	
				return splitSheet[1][5];
			default:
				System.err.println("Error: drawEntitySprite unknown animation frame!");
				return new TextureRegion();
			} // end anim switch-case
		} // end dir switch-case
	} // end method drawEntitySprite
	
	public static void dispose() {
		landTile.dispose();
		waterTile.dispose();
		spriteSheet.dispose();
		
		font.dispose();
		smallFont.dispose();
		systemFont.dispose();
	}

	public static BitmapFont getFont() {
		return font;
	}

	public static BitmapFont getSmallFont() {
		return smallFont;
	}

	public static BitmapFont getSystemFont() {
		return systemFont;
	}
} // end class
