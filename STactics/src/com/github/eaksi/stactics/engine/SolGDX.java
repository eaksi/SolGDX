package com.github.eaksi.stactics.engine;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.eaksi.stactics.db.BattleMap;
import com.github.eaksi.stactics.db.Creature;
import com.github.eaksi.stactics.engine.gfx.Drawable;
import com.github.eaksi.stactics.engine.gfx.Entity;
import com.github.eaksi.stactics.engine.gfx.Entity.Direction;
import com.github.eaksi.stactics.engine.gfx.FontLoader;
import com.github.eaksi.stactics.engine.gfx.Tile;

public class SolGDX extends ApplicationAdapter {
	
	boolean debug = true;		// ****DEBUG****
	boolean drawOrderFlag = false;	// draw order debug
	private int drawOrder = 0;
	
	Camera camera;
	private SpriteBatch batch;			// primary SpriteBatch for graphics
	private SpriteBatch guiBatch;		// GUI SpriteBatch, does not move with camera
	BitmapFont font;					// test font
	BitmapFont smallFont;				// test font 2
	
	private Texture tempTile;			// test tile
	private Texture tempTile0;			// test water tile
	
	private Texture spriteSheet;
	private TextureRegion[][] splitSheet;		// sprite sheet divided into regions
	private TextureRegion[][] flippedSheet;		// same as splitSheet, but regions individually flipped
	
	final int screenWidth = 800;				// screen resolution
	final int screenHeight = 600;
	
	boolean chAnimating = false;				// temp: is the engine animating movement, move keys disabled  
	
	private int tileWidth = 64;
	private int tileHeight = 32;
	private int tileWidthHalf = tileWidth / 2; 		// slight optimization
	private int tileHeightHalf = tileHeight / 2; 	// slight optimization
		
	private BattleMap battleMap;
	private Creature creature;
	
	Vector<Entity> entities;
	int nr = 0;		// current entity Number
	
	Vector<Drawable> painter;
	
	@Override
	public void create () {

		battleMap = new BattleMap();
		creature = new Creature();	// kind of a redundant line, but trying to prevent a future bug
		entities = new Vector<Entity>();
		entities.add(new Entity(creature));
		
		/*** XXX: TEMP ***/
		entities.add(new Entity(creature));
		entities.add(new Entity(creature));
		System.out.println("Entity0 id: "+entities.get(0).getId());
		System.out.println("Entity1 id: "+entities.get(1).getId());
		System.out.println("Entity2 id: "+entities.get(2).getId());
		entities.get(1).tileX = 4;
		entities.get(1).tileY = 5;
		entities.get(2).tileX = 6;
		entities.get(2).tileY = 4;
		/*****************/
		
		// setup camera
		camera = new Camera(screenWidth, screenHeight);
		
		font = FontLoader.getFont(FontLoader.Type.MAIN);
		smallFont = FontLoader.getFont(FontLoader.Type.SMALL);
		
		batch = new SpriteBatch();
		guiBatch = new SpriteBatch();
		
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
        
		// initial setup of entity locations, read tileX&Y, set isoX&Y
		setEntityLocations();
		
		// initialize painter's algorithm (make Drawable objects, sort)
		initializePainter();
        
		// print the BattleMap width and height and w/h difference (negative = more width than height) 
        if (debug) {
        	System.out.println("battleMap.getWidth() = " + battleMap.getWidth() + "  battleMap.getHeight() = " +
        			battleMap.getHeight() + "  difference = " + (battleMap.getWidth()-battleMap.getHeight()));
        }
        	
        
	}

	
	@Override
	public void render () {
		
		// set a white screen
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
 		camera.update();

 		Keyboard.getInput(this);		// keyboard controls handled at this point
 		
		batch.setProjectionMatrix(camera.combined);
	    
		// draw everything
		batch.begin();
		drawIsometric();
	    camera.updateZoom();	 //TODO: move
	    batch.end();  

	    guiBatch.begin();
	    GUI.draw(this, guiBatch);		// draw GUI and possible debug data
	    guiBatch.end();
    	
	    // update FPS counter on window title
	    Gdx.graphics.setTitle("SolGDX     FPS: " + Gdx.graphics.getFramesPerSecond() + " Zoom: " + camera.getZoomLevel());
		
		moveCreature(); 		// XXX: temp character turn-based keyboard movement
	}

	
	private void setEntityLocations()
	{
		// set starting locations of entities
		for (Entity e: entities) {
			e.isoX = getIsoX(e.tileY, e.tileX) + 16; 
	        e.isoY = getIsoY(e.tileY, e.tileX) + 36; //XXX: sprite/tile sizes hack
		}
	}
	
	private void initializePainter() {
		
		painter = new Vector<Drawable>();
		Tile tile;
		
		// add tiles
		for (int i = 0; i < battleMap.getWidth(); i++) {
			for (int j = 0; j < battleMap.getHeight(); j++) {
				if (battleMap.getTile(i,j) == 0 ) {
					tile = new Tile(i, j, getIsoX(j,i), getIsoY(j,i), getIsoY(j,i), true);
					painter.add((Drawable)tile);
					
				} else {
					
					tile = new Tile(i, j, getIsoX(j,i), getIsoY(j,i)+battleMap.getTile(i,j)*16, getIsoY(j,i), false);
					painter.add((Drawable)tile);
					/*for (int k = -1; k < battleMap.getTile(i,j); k++) {	// TODO: temp, change to working wall graphics
						tile = new Tile(getIsoX(j,i), getIsoY(j,i)+k*16+16, getIsoY(j,i), false);
						painter.add((Drawable)tile);
						if (debug) System.out.println("draw tile: (" + tile.isoX + "," + tile.isoY + ") z-depth: " + tile.getZ() +
								" height: " + k + " water: "+tile.isWater());
					}*/
				}
				//debug
				if (debug) System.out.println("draw tile: (" + tile.tileX + "," + tile.tileY + ") z-depth: " + tile.getZ() +
						" height: " + battleMap.getTile(i,j) + " water: "+tile.isWater());
			}
		}
		
		// add characters
		for (Entity e: entities) {
			e.setZ(e.isoY);
			painter.add(e);
		}
		
		//FIXME: sorting goes here!
		Collections.sort((List<Drawable>) painter, Collections.reverseOrder());
		
	}

	// check if creature can move to a tile, this fires only once per move
	void setMoveDirection(Direction d) {

		boolean canMove = false;
		entities.get(nr).setHeading(d);
		
		switch (entities.get(nr).getHeading()) {
		case NE:
			if (battleMap.getTile((entities.get(nr).tileX-1), entities.get(nr).tileY) == 1) {
				canMove = true;
				printMoveDebug("NE (-x)", -1, 0);
				entities.get(nr).tileX--;
			}
			break;
		case SE:
			if (battleMap.getTile(entities.get(nr).tileX, (entities.get(nr).tileY+1)) == 1) {
				canMove = true;
				printMoveDebug("SE (+y)", 0, 1);
				entities.get(nr).tileY++;
			}
			break;
		case SW:
			if (battleMap.getTile((entities.get(nr).tileX+1), entities.get(nr).tileY) == 1) {
				canMove = true;
				printMoveDebug("SW (+x)", 1, 0);
				entities.get(nr).tileX++;
			}
			break;
		case NW:
			if (battleMap.getTile(entities.get(nr).tileX, (entities.get(nr).tileY-1)) == 1) {
				canMove = true;
				printMoveDebug("NW (-y)", 0, -1);
				entities.get(nr).tileY--;
			}
			break;
		default:
			break;
		}
		
		if (canMove) {
    		entities.get(nr).animFrame = 0;
    		entities.get(nr).setAnimation(Entity.Animation.WALK);
    		chAnimating = true;

		}
	}

	
	private void printMoveDebug (String dir, int x, int y) {
		System.out.println(dir + "(" + entities.get(nr).tileX + "," + entities.get(nr).tileY + ") -> (" +
				(entities.get(nr).tileX + x) + "," + (entities.get(nr).tileY + y) + ")");
	}
	
	//move rectangle/sprite
	private void moveCreature() {
		
		if (chAnimating) {
			switch (entities.get(nr).getHeading()) {
			case NE:
		    	entities.get(nr).isoX += 2;
		    	camera.moveRight(2);
		    	entities.get(nr).isoY += 1;
		    	camera.moveUp(1);
				break;
			case SE:
		    	entities.get(nr).isoX += 2;
		    	camera.moveRight(2);
		    	entities.get(nr).isoY -= 1;
		    	camera.moveDown(1);
				break;
			case SW:
		    	entities.get(nr).isoX -= 2;
		    	camera.moveLeft(2);
		    	entities.get(nr).isoY -= 1;
		    	camera.moveDown(1);
				break;
			case NW:
		    	entities.get(nr).isoX -= 2;
		    	camera.moveLeft(2);
		    	entities.get(nr).isoY += 1;
		    	camera.moveUp(1);
				break;
			default:
				break;
			}

			entities.get(nr).animFrame++;
			
			if (entities.get(nr).animFrame >= 16) {
				entities.get(nr).animFrame = -1;
				chAnimating = false;
				entities.get(nr).setAnimation(Entity.Animation.IDLE);
			}
			
		}
	}
	
	
	// Get the isometric projection coordinate X, given tilemap X and Y as parameters.
    private int getIsoX(int mapx, int mapy) {
    	return (screenWidth - ((battleMap.getWidth() * 32 - 32) + (mapy - mapx) * tileWidthHalf));
    	//return (screenWidth - ((battleMap.getWidth() * 32 - 32) + (mapx - mapy) * tileWidthHalf));
    			
    }

    
	// Get the isometric projection coordinate Y, given tilemap X and Y as parameters.
    private int getIsoY(int mapx, int mapy) {
    	return (screenHeight - ((battleMap.getHeight() * 16 - 32) + (mapy + mapx) * tileHeightHalf));
    			
    }

    // TODO: Draw everything
    private void drawIsometric() {

    	//FIXME: sorting goes here!
    	
		drawOrder = 0;
		smallFont.setColor(0f, 0f, 0f, 1f);
		
		for (Drawable d: painter) {
			if (d instanceof Tile) {
				if (((Tile)d).isWater()) {
					batch.draw(tempTile0, d.isoX, d.isoY);
				} else {
					batch.draw(tempTile, d.isoX, d.isoY);
				}

				/*if (drawOrderFlag) {
					smallFont.draw(batch, ""+drawOrder, d.isoX+25, d.isoY);
				}*/
				
			} else if (d instanceof Entity) {
				drawCharacter((Entity)d);
			} else {
				System.err.println("ERROR: unknown class in Vector<Drawable> painter");
			}
		}
		
		/*
		for (int i = 0; i < battleMap.getWidth(); i++) {
			for (int j = 0; j < battleMap.getHeight(); j++) {
				drawOrder++;
				if (battleMap.getTile(i,j) == 0 ) {
					batch.draw(tempTile0, getIsoX(j,i), getIsoY(j,i));	
				} else {
					for (int k = -1; k < battleMap.getTile(i,j); k++) {	// TODO: temp, change to working wall graphics
						batch.draw(tempTile, getIsoX(j,i), getIsoY(j,i)+k*16+16);
					}
					//batch.draw(tempTile, getIsoX(j,i), (getIsoY(j,i)+(battleMap.getTile(i,j)*16)));
				}
				if (drawOrderFlag) {
					smallFont.draw(batch, ""+drawOrder, getIsoX(j,i)+25, (getIsoY(j,i)+(battleMap.getTile(i,j)*16+36)));
				}
			}
		}

		drawCharacter(entities.get(0));
		drawCharacter(entities.get(1));
		drawCharacter(entities.get(2));
	*/
    	// Draw BattleMap coordinates over tiles
    	if (debug) {
    		smallFont.setColor(0f, 0f, 0f, 1f);
        	for (int i = 0; i < battleMap.getWidth(); i++) {
            	for (int j = 0; j < battleMap.getHeight(); j++) {
        				smallFont.draw(batch, i+","+j, getIsoX(j,i)+25, (getIsoY(j,i)+(battleMap.getTile(i,j)*16+36)));	
           		}
        	}
    	}
    	if (drawOrderFlag) {
			for (Drawable d: painter) {
				drawOrder++;
				if (d instanceof Tile) {
					smallFont.draw(batch, ""+drawOrder, d.isoX+25, d.isoY+32);
				}
			}
    	}
    	/*if (drawOrderFlag) {
    		smallFont.setColor(0f, 0f, 0f, 1f);
        	for (int i = 0; i < battleMap.getWidth(); i++) {
            	for (int j = 0; j < battleMap.getHeight(); j++) {
        				smallFont.draw(batch, ""+drawOrder, getIsoX(j,i)+25, (getIsoY(j,i)+(battleMap.getTile(i,j)*16+36)));	
           		}
        	}
    	}*/
    }
    
     // FIXME: this whole method
    private void drawCharacter(Entity e) {
		    	
    	if (e.animFrame == 3 || e.animFrame == 11) { // XXX: temp simulate movement
			e.isoY += 1;
		} else if (e.animFrame == 5 || e.animFrame == 14) {
			e.isoY -= 1;
		}
    	
    	switch(e.animFrame) {
    	case -1: case 0: case 1: case 7: case 8: case 9: case 15:
    	    switch(e.getHeading()) {
	    	case NE:
	    		batch.draw(flippedSheet[0][3], e.isoX, e.isoY);
	    		break;
	    	case SE:
	    		batch.draw(splitSheet[0][0], e.isoX, e.isoY);
	    		break;
	    	case SW:
	    		batch.draw(flippedSheet[0][0], e.isoX, e.isoY);
	    		break;
	    	case NW:
	    		batch.draw(splitSheet[0][3], e.isoX, e.isoY);
	    		break;
	    	default:
	    		System.err.println("Warning: invalid direction of character!"); //XXX: written 60 times per second
	    	}
    	    break;
    	case 2: case 3: case 4: case 5: case 6:	//walk 1
        	switch(e.getHeading()) {
        	case NE:
        		batch.draw(flippedSheet[0][4], e.isoX, e.isoY);
        		break;
        	case SE:
        		batch.draw(splitSheet[0][1], e.isoX, e.isoY);
        		break;
        	case SW:
        		batch.draw(flippedSheet[0][1], e.isoX, e.isoY);
        		break;
        	case NW:
        		batch.draw(splitSheet[0][4], e.isoX, e.isoY);
        		break;
        	default:
        		System.err.println("Warning: invalid direction of character!"); //XXX: written 60 times per second
        	}
    		break;
    	case 10: case 11: case 12: case 13: case 14: // walk 2
        	switch(e.getHeading()) {
        	case NE:
        		batch.draw(flippedSheet[0][5], e.isoX, e.isoY);
        		break;
        	case SE:
        		batch.draw(splitSheet[0][2], e.isoX, e.isoY);
        		break;
        	case SW:
        		batch.draw(flippedSheet[0][2], e.isoX, e.isoY);
        		break;
        	case NW:
        		batch.draw(splitSheet[0][5], e.isoX, e.isoY);
        		break;
        	default:
        		System.err.println("Warning: invalid direction of character!"); //XXX: written 60 times per second
        	}
    		break;
    	default:
    		System.err.println("Warning: invalid animation frame!"); //XXX: written 60 times per second
    			
    	}
    	
    }


	@Override
	public void dispose() {
		batch.dispose();
		guiBatch.dispose();
		tempTile.dispose();
		spriteSheet.dispose();
		font.dispose();
		smallFont.dispose();
	}


	
}
