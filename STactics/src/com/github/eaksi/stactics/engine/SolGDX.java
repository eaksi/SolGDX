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
	
	boolean debug = false;								// ****DEBUG****
	boolean drawOrderFlag = false;						// draw order debug
	private boolean cameraFollowsCharacter = false;		// set camera to move with character
	private int drawOrder = 0;
	
	Camera camera;
	private SpriteBatch batch;			// primary SpriteBatch for graphics
	private SpriteBatch guiBatch;		// GUI SpriteBatch, does not move with camera
	BitmapFont font;					// temp test font
	BitmapFont smallFont;				// temp test font 2
	
	private Texture tempTile;			// temp test tile
	private Texture tempTile0;			// temp test water tile
	
	private Texture spriteSheet;
	private TextureRegion[][] splitSheet;		// sprite sheet divided into regions
	private TextureRegion[][] flippedSheet;		// same as splitSheet, but regions individually flipped
	
	final int screenWidth = 1024;				// screen resolution
	final int screenHeight = 800;
	
	boolean chAnimating = false;				// temp: is the engine animating movement, move keys disabled  

	private int tileWidth = 64;
	private int tileHeight = 32;
		
	private BattleMap battleMap;
		
	Vector<Entity> entities;
	int nr = 0;		// current entity Number
	
	Vector<Drawable> painter;

	// debug
	public int mouseScreenX = -999;
	public int mouseScreenY = -999;
	public int mouseIsoX = -999;
	public int mouseIsoY = -999;
	public int mouseDragX = -999;
	public int mouseDragY = -999;

	@Override
	public void create () {
		
		battleMap = new BattleMap();
		entities = new Vector<Entity>();
		entities.add(new Entity(new Creature()));
		
		/*** XXX: TEMP ***/
		entities.add(new Entity(new Creature()));
		entities.add(new Entity(new Creature()));
		System.out.println("Entity0 id: "+entities.get(0).getId());
		System.out.println("Entity1 id: "+entities.get(1).getId());
		System.out.println("Entity2 id: "+entities.get(2).getId());
		entities.get(1).tileX = 4;
		entities.get(1).tileY = 5;
		entities.get(2).tileX = 6;
		entities.get(2).tileY = 4;
		System.out.println("***ENTITIES***");
		for (Entity e: entities) {
			System.out.println(e.cr.getId() + ": " + e.cr.getFullName());
		}
		System.out.println("**************");
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

 		Controls.getInput(this);		// keyboard controls handled at this point
 		
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
				} else {
					tile = new Tile(i, j, getIsoX(j,i), getIsoY(j,i)+battleMap.getTile(i,j)*16, getIsoY(j,i), false);
				}
				
				painter.add((Drawable)tile);
				
				//debug
				if (debug) System.out.println("draw tile: (" + tile.tileX + "," + tile.tileY + ") z-depth: " + tile.getZ() +
						" height: " + battleMap.getTile(i,j) + " water: "+tile.isWater());
			}
		}
		
		// add characters
		for (Entity e: entities) {
			//FIXME: z position guessed (but tested, need frame by frame analysis, height changes testing and gfx)
			e.setZ(e.isoY-43);
			painter.add(e);
		}
		
		// initial sorting
		Collections.sort((List<Drawable>) painter, Collections.reverseOrder());
		
	}

	// check if creature can move to a tile, this fires only once per move
	void setMoveDirection(Direction d) {

		boolean canMove = false;
		int tryX = 0;
		int tryY = 0;

		entities.get(nr).setHeading(d);
		
		switch (entities.get(nr).getHeading()) {
		case NE: 
			tryX = -1;
			break;
		case SE:
			tryY = 1;
			break;
		case SW:
			tryX = 1;
			break;
		case NW:
			tryY = -1;
			break;
		default:
			break;
		}
		
		if (battleMap.getTile((entities.get(nr).tileX + tryX), entities.get(nr).tileY + tryY) == 1) {
			canMove = true;
		} else {
			canMove = false;
			printMoveDebug("Can't move (terrain): " + entities.get(nr).getHeading(), tryX, tryY);
			return;		// NOTE!: "return;" here
		}
		
		for (Entity e: entities) {
			if (entities.get(nr).tileX + tryX  == e.tileX  &&  entities.get(nr).tileY + tryY == e.tileY) {
				canMove = false;
				printMoveDebug("Can't move, " + e.cr.getName() + " (id:"+ e.cr.getId() + ") is in the way: " +
						entities.get(nr).getHeading(), tryX, tryY);
				return; 	// NOTE!: "return;" here
			}
		}
		
		
		if (canMove) {
			printMoveDebug("" + entities.get(nr).getHeading(), tryX, tryY);
			entities.get(nr).tileX += tryX;
			entities.get(nr).tileY += tryY;
			
			// if everything okay, set movement in motion 
    		entities.get(nr).animFrame = 0;
    		entities.get(nr).setAnimation(Entity.Animation.WALK);
    		chAnimating = true;
		}
	}

	
	private void printMoveDebug (String dir, int x, int y) {
		System.out.println(dir + "(" + entities.get(nr).tileX + "," + entities.get(nr).tileY + ") -> (" +
				(entities.get(nr).tileX + x) + "," + (entities.get(nr).tileY + y) + ")");
	}
	
	// Moves the character sprite on screen
	private void moveCreature() {

		int modX = 0;
		int modY = 0;
		int modZ = 0;
		
		if (chAnimating) {
			switch (entities.get(nr).getHeading()) {
			case NE:
				modX = 2;
				modY = 1;
				modZ = 1;
				break;
			case SE:
				modX = 2;
				modY = -1;
				modZ = -1;
				break;
			case SW:
				modX = -2;
				modY = -1;
				modZ = -1;
				break;
			case NW:
				modX = -2;
				modY = 1;
				modZ = 1;
				break;
			default:
				break;
			}

			entities.get(nr).isoX += modX;
	    	entities.get(nr).isoY += modY;
	    	entities.get(nr).z += modZ;
	    	
	    	if (cameraFollowsCharacter) {
	    		camera.moveHorizontal(modX);
	    		camera.moveVertical(modY);
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
    	return (screenWidth - ((battleMap.getWidth() * 32 - 32) + (mapy - mapx) * (tileWidth / 2)));
    }

    
	// Get the isometric projection coordinate Y, given tilemap X and Y as parameters.
    private int getIsoY(int mapx, int mapy) {
    	return (screenHeight - ((battleMap.getHeight() * 16 - 32) + (mapy + mapx) * (tileHeight / 2)));
    }

    // TODO: Draw everything
    private void drawIsometric() {

    	// sort everything (again)
    	// TODO: sorting optimization
    	Collections.sort((List<Drawable>) painter, Collections.reverseOrder());
    	
		drawOrder = 0;
		smallFont.setColor(0f, 0f, 0f, 1f);
		
		for (Drawable d: painter) {
			if (d instanceof Tile) {
				if (((Tile)d).isWater()) {
					batch.draw(tempTile0, d.isoX, d.isoY);
				} else {
					batch.draw(tempTile, d.isoX, d.isoY);
				}
			} else if (d instanceof Entity) {
				drawCharacter((Entity)d);
			} else {
				System.err.println("ERROR: unknown class in Vector<Drawable> painter");
			}
		}
		
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
					smallFont.draw(batch, ""+drawOrder, d.isoX+24, d.isoY+36);
			}
    	}
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
		tempTile0.dispose();
		spriteSheet.dispose();
		
		font.dispose();
		smallFont.dispose();
	}

	
} // end class SolGDX
