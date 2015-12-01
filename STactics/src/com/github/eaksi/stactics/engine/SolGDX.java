package com.github.eaksi.stactics.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.github.eaksi.stactics.db.BattleMap;
import com.github.eaksi.stactics.db.Creature;
import com.github.eaksi.stactics.engine.gfx.Entity;
import com.github.eaksi.stactics.engine.gfx.Entity.Direction;
import com.github.eaksi.stactics.engine.gfx.FontLoader;

public class SolGDX extends ApplicationAdapter {
	
	private boolean debugFlag = false;
	
	Camera camera;
	private SpriteBatch batch;			// primary SpriteBatch for graphics
	private SpriteBatch guiBatch;		// GUI SpriteBatch, does not move with camera
	private BitmapFont font;			// test font
	private BitmapFont smallFont;		// test font 2
    private GlyphLayout layout = new GlyphLayout();	// XXX: temp GUI testing
	
	private Texture tempTile;			// test tile
	
	private Texture spriteSheet;
	private TextureRegion[][] splitSheet;		// sprite sheet divided into regions
	private TextureRegion[][] flippedSheet;		// same as splitSheet, but regions individually flipped
	private final int screenWidth = 640;
	private final int screenHeight = 480;
	
	boolean chAnimating = false;				// temp: is the engine animating movement, move keys disabled  
	
	private int tileWidth = 64;
	private int tileHeight = 32;
	private int tileWidthHalf = tileWidth / 2; 		// slight optimization
	private int tileHeightHalf = tileHeight / 2; 	// slight optimization
		
	private BattleMap battleMap;
	private Creature creature;
	private Entity entity;
		

	@Override
	public void create () {

		battleMap = new BattleMap();
		creature = new Creature();	// kind of a redundant line, but trying to prevent a future bug
		entity = new Entity(creature);
		
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
        
        entity.isoX = getIsoX(entity.tileY, entity.tileX) + 16; 
        entity.isoY = getIsoY(entity.tileY, entity.tileX) + 36; //XXX: sprite/tile sizes hack
        
        System.out.println("battleMap.getWidth() = " + battleMap.getWidth() + "  battleMap.getHeight() = " + battleMap.getHeight() + 
        		"  difference = " + (battleMap.getWidth()-battleMap.getHeight()));
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
	    drawTiles();
	    drawCharacters();
	    
	    camera.updateZoom();
	    batch.end();  

	    guiBatch.begin();
	    drawGUI();
	    if (debugFlag) drawDebug();
	    guiBatch.end();
    	
	    // update FPS counter on window title
	    Gdx.graphics.setTitle("SolGDX     FPS: " + Gdx.graphics.getFramesPerSecond() + " Zoom: " + camera.getZoomLevel());
		
		moveCreature(); 		// XXX: temp character turn-based keyboard movement
	}


	// check if creature can move to a tile, this fires only once per move
	void setMoveDirection(Direction d) {

		boolean canMove = false;
		entity.setHeading(d);
		
		switch (entity.getHeading()) {
		case NE:
			if (battleMap.getTile((entity.tileX-1), entity.tileY) == 1) {
				canMove = true;
				printMoveDebug("NE (-x)", -1, 0);
				entity.tileX--;
			}
			break;
		case SE:
			if (battleMap.getTile(entity.tileX, (entity.tileY+1)) == 1) {
				canMove = true;
				printMoveDebug("SE (+y)", 0, 1);
				entity.tileY++;
			}
			break;
		case SW:
			if (battleMap.getTile((entity.tileX+1), entity.tileY) == 1) {
				canMove = true;
				printMoveDebug("SW (+x)", 1, 0);
				entity.tileX++;
			}
			break;
		case NW:
			if (battleMap.getTile(entity.tileX, (entity.tileY-1)) == 1) {
				canMove = true;
				printMoveDebug("NW (-y)", 0, -1);
				entity.tileY--;
			}
			break;
		default:
			break;
		}
		
		if (canMove) {
    		entity.animFrame = 0;
    		entity.setAnimation(Entity.Animation.WALK);
    		chAnimating = true;
		}
	}

	
	private void printMoveDebug (String dir, int x, int y) {
		System.out.println(dir + "(" + entity.tileX + "," + entity.tileY + ") -> (" +
				(entity.tileX + x) + "," + (entity.tileY + y) + ")");
	}
	
	//move rectangle/sprite
	private void moveCreature() {
		
		if (chAnimating) {
			switch (entity.getHeading()) {
			case NE:
		    	entity.isoX += 2;
		    	entity.isoY += 1;
				break;
			case SE:
		    	entity.isoX += 2;
		    	entity.isoY -= 1;
				break;
			case SW:
		    	entity.isoX -= 2;
		    	entity.isoY -= 1;
				break;
			case NW:
		    	entity.isoX -= 2;
		    	entity.isoY += 1;
				break;
			default:
				break;
			}

			entity.animFrame++;
			
			if (entity.animFrame >= 16) {
				entity.animFrame = -1;
				chAnimating = false;
				entity.setAnimation(Entity.Animation.IDLE);
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

    
	// Draw the tile map
	private void drawTiles() {

    	for (int i = 0; i < battleMap.getWidth(); i++) {
        	for (int j = 0; j < battleMap.getHeight(); j++) {
    			if (battleMap.getTile(i,j) != 0 ) {
    				batch.draw(tempTile, getIsoX(j,i), (getIsoY(j,i)+16));	
    			}
    		}
    	}
    }
	
	
    // FIXME: this whole method
    private void drawCharacters() {
		if (entity.animFrame == 3 || entity.animFrame == 11) { // XXX: temp simulate movement
			entity.isoY += 1;
		} else if (entity.animFrame == 5 || entity.animFrame == 14) {
			entity.isoY -= 1;
		}
    	
    	switch(entity.animFrame) {
    	case -1: case 0: case 1: case 7: case 8: case 9: case 15:
    	    switch(entity.getHeading()) {
	    	case NE:
	    		batch.draw(flippedSheet[0][3], entity.isoX, entity.isoY);
	    		break;
	    	case SE:
	    		batch.draw(splitSheet[0][0], entity.isoX, entity.isoY);
	    		break;
	    	case SW:
	    		batch.draw(flippedSheet[0][0], entity.isoX, entity.isoY);
	    		break;
	    	case NW:
	    		batch.draw(splitSheet[0][3], entity.isoX, entity.isoY);
	    		break;
	    	default:
	    		System.err.println("Warning: invalid direction of character!"); //XXX: written 60 times per second
	    	}
    	    break;
    	case 2: case 3: case 4: case 5: case 6:	//walk 1
        	switch(entity.getHeading()) {
        	case NE:
        		batch.draw(flippedSheet[0][4], entity.isoX, entity.isoY);
        		break;
        	case SE:
        		batch.draw(splitSheet[0][1], entity.isoX, entity.isoY);
        		break;
        	case SW:
        		batch.draw(flippedSheet[0][1], entity.isoX, entity.isoY);
        		break;
        	case NW:
        		batch.draw(splitSheet[0][4], entity.isoX, entity.isoY);
        		break;
        	default:
        		System.err.println("Warning: invalid direction of character!"); //XXX: written 60 times per second
        	}
    		break;
    	case 10: case 11: case 12: case 13: case 14: // walk 2
        	switch(entity.getHeading()) {
        	case NE:
        		batch.draw(flippedSheet[0][5], entity.isoX, entity.isoY);
        		break;
        	case SE:
        		batch.draw(splitSheet[0][2], entity.isoX, entity.isoY);
        		break;
        	case SW:
        		batch.draw(flippedSheet[0][2], entity.isoX, entity.isoY);
        		break;
        	case NW:
        		batch.draw(splitSheet[0][5], entity.isoX, entity.isoY);
        		break;
        	default:
        		System.err.println("Warning: invalid direction of character!"); //XXX: written 60 times per second
        	}
    		break;
    	default:
    		System.err.println("Warning: invalid animation frame!"); //XXX: written 60 times per second
    			
    	}
    	
    }
    
    
    // Draw the user interface
    private void drawGUI() {

     	/*// FIXME: quick hack for white text outline
    	font.setColor(1f, 1f, 1f, 1f);
     	font.draw(guiBatch, "NPC: "+ entity.cr.getFullName(), 9, screenHeight-9);
     	font.draw(guiBatch, "NPC: "+ entity.cr.getFullName(), 9, screenHeight-11);
     	font.draw(guiBatch, "NPC: "+ entity.cr.getFullName(), 11, screenHeight-9);
     	font.draw(guiBatch, "NPC: "+ entity.cr.getFullName(), 11, screenHeight-11);
     	font.draw(guiBatch, "NPC: "+ entity.cr.getFullName(), 10, screenHeight-9);
     	font.draw(guiBatch, "NPC: "+ entity.cr.getFullName(), 10, screenHeight-11);
     	font.draw(guiBatch, "NPC: "+ entity.cr.getFullName(), 11, screenHeight-10);
     	font.draw(guiBatch, "NPC: "+ entity.cr.getFullName(), 9, screenHeight-10);
     	 */
     	
    	font.setColor(0.6f, 0.6f, 0.6f, 1f);
     	font.draw(guiBatch, "name: "+ entity.cr.getFullName(), 10, screenHeight-10);
     	smallFont.setColor(0.6f, 0.6f, 0.6f, 1f);
     	smallFont.draw(guiBatch, "framesLeft: "+ entity.getFramesLeft(), 10, screenHeight-30);
     	smallFont.draw(guiBatch, "animation: "+ entity.getAnimString(), 10, screenHeight-45);
     	

     	
     	// debug info
     	smallFont.setColor(0f, 0f, 0f, 1f);
     	smallFont.draw(guiBatch, ("entity.isoX: "+entity.isoX), 10, screenHeight-15, screenWidth-20, Align.right, true);
    	smallFont.draw(guiBatch, ("entity.isoY: "+entity.isoY), 10, screenHeight-30, screenWidth-20, Align.right, true);
    	smallFont.draw(guiBatch, ("entity.tileX: "+entity.tileX), 10, screenHeight-45, screenWidth-20, Align.right, true);
    	smallFont.draw(guiBatch, ("entity.tileY: "+entity.tileY), 10, screenHeight-60, screenWidth-20, Align.right, true);

    	//layout.setText(font, "layout test");
    	//font.draw(guiBatch, layout, 200 + layout.width / 3, 200 + layout.height / 3);


    }
    
    
	// Draw the needed stuff for debug (for quick testing)
	private void drawDebug() {
		
		// draw the original sprite sheet by region
		for (int i=0; i<2; i++) {
			for (int j=0; j<7; j++) {
				guiBatch.draw(splitSheet[i][j], 240+j*32 , 64-i*64);
			}
		}
		
		// draw the flipped sprite sheet by region
	    for (int i=0; i<2; i++) {
			for (int j=0; j<7; j++) {
				guiBatch.draw(flippedSheet[i][j], j*32, 64-i*64);
			}
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
