package com.github.eaksi.stactics.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.github.eaksi.stactics.db.BattleMap;
import com.github.eaksi.stactics.db.Creature;
import com.github.eaksi.stactics.engine.gfx.Entity;
import com.github.eaksi.stactics.engine.gfx.FontLoader;

public class SolGDX extends ApplicationAdapter {
	
	public enum TempDirection {
		NE, SE, SW, NW
	}
	
	Camera camera;
	private SpriteBatch batch;
	private SpriteBatch guiBatch;
	private BitmapFont font;
    private GlyphLayout layout = new GlyphLayout();	// XXX: temp GUI testing
	
	private Texture tempTile;
	
	private Texture spriteSheet;
	private TextureRegion[][] splitSheet;
	private TextureRegion[][] flippedSheet;
	private Rectangle charRect;
	private final int screenWidth = 640;
	private final int screenHeight = 480;
	
	boolean chAnimating = false;
	private TempDirection chMovingDirection = TempDirection.NE;
	private int chMoveFrame = -1;
	
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
		
		font = FontLoader.getFont();
		
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
        
        charRect = new Rectangle();
        charRect.x = getIsoX(entity.tileY, entity.tileX) + 16; 
        charRect.y = getIsoY(entity.tileY, entity.tileX) + 36; //XXX: sprite/tile sizes hack
        charRect.width = 32;
        charRect.height = 64;
        
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
	    //drawDebug();
	    camera.updateZoom();
	    

    	guiBatch.begin();
	    drawGUI();
	    batch.end();
	    guiBatch.end();
	    
    	
	    // update FPS counter on window title
	    Gdx.graphics.setTitle("SolGDX     FPS: " + Gdx.graphics.getFramesPerSecond() + " Zoom: " + camera.getZoomLevel());
		
		moveCreature(); 		// XXX: temp character turn-based keyboard movement
	}




	// check if creature can move to a tile, this fires only once per move
	void setMoveDirection(TempDirection d) {

		boolean canMove = false;
		chMovingDirection = d;
		
		switch (chMovingDirection) {
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
    		chMoveFrame = 0;
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
			switch (chMovingDirection) {
			case NE:
		    	charRect.x += 2;
		    	charRect.y += 1;
				break;
			case SE:
		    	charRect.x += 2;
		    	charRect.y -= 1;
				break;
			case SW:
		    	charRect.x -= 2;
		    	charRect.y -= 1;
				break;
			case NW:
		    	charRect.x -= 2;
		    	charRect.y += 1;
				break;
			default:
				break;
			}

			chMoveFrame++;
			
			if (chMoveFrame >= 16) {
				chMoveFrame = -1;
				chAnimating = false;
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
		if (chMoveFrame == 3 || chMoveFrame == 11) { // XXX: temp simulate movement
			charRect.y += 1;
		} else if (chMoveFrame == 5 || chMoveFrame == 14) {
			charRect.y -= 1;
		}
    	
    	switch(chMoveFrame) {
    	case -1: case 0: case 1: case 7: case 8: case 9: case 15:
    	    switch(chMovingDirection) {
	    	case NE:
	    		batch.draw(flippedSheet[0][3], charRect.x, charRect.y);
	    		break;
	    	case SE:
	    		batch.draw(splitSheet[0][0], charRect.x, charRect.y);
	    		break;
	    	case SW:
	    		batch.draw(flippedSheet[0][0], charRect.x, charRect.y);
	    		break;
	    	case NW:
	    		batch.draw(splitSheet[0][3], charRect.x, charRect.y);
	    		break;
	    	default:
	    		System.err.println("Warning: invalid direction of character!"); //XXX: written 60 times per second
	    	}
    	    break;
    	case 2: case 3: case 4: case 5: case 6:	//walk 1
        	switch(chMovingDirection) {
        	case NE:
        		batch.draw(flippedSheet[0][4], charRect.x, charRect.y);
        		break;
        	case SE:
        		batch.draw(splitSheet[0][1], charRect.x, charRect.y);
        		break;
        	case SW:
        		batch.draw(flippedSheet[0][1], charRect.x, charRect.y);
        		break;
        	case NW:
        		batch.draw(splitSheet[0][4], charRect.x, charRect.y);
        		break;
        	default:
        		System.err.println("Warning: invalid direction of character!"); //XXX: written 60 times per second
        	}
    		break;
    	case 10: case 11: case 12: case 13: case 14: // walk 2
        	switch(chMovingDirection) {
        	case NE:
        		batch.draw(flippedSheet[0][5], charRect.x, charRect.y);
        		break;
        	case SE:
        		batch.draw(splitSheet[0][2], charRect.x, charRect.y);
        		break;
        	case SW:
        		batch.draw(flippedSheet[0][2], charRect.x, charRect.y);
        		break;
        	case NW:
        		batch.draw(splitSheet[0][5], charRect.x, charRect.y);
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
    // FIXME: user interface moves with camera
    private void drawGUI() {
 

     	font.setColor(0.4f, 0.4f, 0.8f, 1f);
    	//font.draw(batch, "align test align test align test", 10, screenHeight-80, screenWidth-20, Align.right, true);

    	//layout.setText(font, "layout test");
    	//font.draw(batch, layout, 200 + layout.width / 3, 200 + layout.height / 3);
     	font.draw(batch, "NPC: "+ entity.cr.getFullName(), 10, screenHeight-10);
    }
    
    
	// Draw the needed stuff for debug (for quick testing)
	private void drawDebug() {
		
		// draw the original sprite sheet by region
		for (int i=0; i<2; i++) {
			for (int j=0; j<7; j++) {
				batch.draw(splitSheet[i][j], 240+j*32 , 64-i*64);
			}
		}
		
		// draw the flipped sprite sheet by region
	    for (int i=0; i<2; i++) {
			for (int j=0; j<7; j++) {
				batch.draw(flippedSheet[i][j], j*32, 64-i*64);
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
	}


	
}
