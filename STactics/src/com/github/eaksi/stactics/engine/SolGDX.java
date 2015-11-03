package com.github.eaksi.stactics.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class SolGDX extends ApplicationAdapter {
	
	public enum Direction {
		NE, SE, SW, NW
	}
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	
	private Texture tempTile;
	
	private Texture spriteSheet;
	private TextureRegion[][] splitSheet;
	private TextureRegion[][] flippedSheet;
	private Rectangle charRect;
	private final int screenWidth = 640;
	private final int screenHeight = 480;
	
	private boolean chAnimating = false;
	private Direction chMovingDirection = Direction.NE;
	private int chMoveFrame = -1;
	
	private int tileWidth = 64;
	private int tileHeight = 32;
	private int tileWidthHalf = tileWidth / 2; 		// slight optimization
	private int tileHeightHalf = tileHeight / 2; 	// slight optimization
	
	private int tileMap[][] = {	{1,1,1,1,1,1},
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
	
	int tileMapWidth = tileMap.length; // XXX: arrays always rectangular, checked elsewhere
	int tileMapHeight = tileMap[0].length;
	int tileMapWidthHeightDifference = tileMapWidth - tileMapHeight;

	
	@Override
	public void create () {
		
		// setup camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenWidth, screenHeight);

		batch = new SpriteBatch();
		
		spriteSheet = new Texture("data/farcher_placeholder.png");
		splitSheet = TextureRegion.split(spriteSheet, 32, 64);
		flippedSheet = TextureRegion.split(spriteSheet, 32, 64);

		// flip all sprites within sheet 
		for (int i=0; i < 2; i++) {
			for (int j=0; j<7; j++) {
				flippedSheet[i][j].flip(true, false);
			}
		}
		
		tempTile = new Texture("data/64px_tile_placeholder.png");
		font = new BitmapFont();
        font.setColor(Color.BLACK);
        
        charRect = new Rectangle();
        charRect.x = getIsoX(0,2) + 16; 
        charRect.y = getIsoY(0,2) + 36; //XXX: sprite/tile sizes hack
        charRect.width = 32;
        charRect.height = 64;
        
        System.out.println("tileMapWidth = " + tileMapWidth + "  tileMapHeight = " + tileMapHeight + 
        		"  difference = " + tileMapWidthHeightDifference);
	}

	
	@Override
	public void render () {
		
		// set a white screen
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
 		camera.update();

		batch.setProjectionMatrix(camera.combined);
	    
	    // draw everything
	    batch.begin();
	    drawTiles();
	    drawCharacters();
	    drawDebug();
	    batch.end();
		
	    // update FPS counter on window title
	    Gdx.graphics.setTitle("SolGDX     FPS: " + Gdx.graphics.getFramesPerSecond());
				
		getKeyboardInputs();	// keyboard controls handled at this point

		moveCharRect(); 		// XXX: temp character turn-based keyboard movement
	}
	
	private void setMoveDirection(Direction d) {
    	chMovingDirection = d;
    	chMoveFrame = 0;
    	chAnimating = true;
	}
	
	
	public void getKeyboardInputs() {	// simplified, press multiple buttons at once etc.

		if (chAnimating) return;
				
	    if(Gdx.input.isKeyPressed(Keys.UP))		setMoveDirection(Direction.NE);
	    if(Gdx.input.isKeyPressed(Keys.DOWN))	setMoveDirection(Direction.SW);
	    if(Gdx.input.isKeyPressed(Keys.LEFT))	setMoveDirection(Direction.NW);
	    if(Gdx.input.isKeyPressed(Keys.RIGHT))	setMoveDirection(Direction.SE);

	    if(Gdx.input.isKeyPressed(Keys.Q))		Gdx.app.exit();
	    
	}
	
	
	//move rectangle/sprite
	private void moveCharRect() {
		
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
	
	// Draw the needed stuff for debug (for quick testing)
	private void drawDebug() {
		
		// draw the original sprite sheet by region
		for (int i=0; i<2; i++) {
			for (int j=0; j<7; j++) {
				batch.draw(splitSheet[i][j],240+j*32,64-i*64);
			}
		}
		
		// draw the flipped sprite sheet by region
	    for (int i=0; i<2; i++) {
			for (int j=0; j<7; j++) {
				batch.draw(flippedSheet[i][j],j*32,64-i*64);
			}
		}

	}
	
	// Draw the tile map
	private void drawTiles() {

    	for (int i = 0; i < tileMapWidth; i++) {
        	for (int j = 0; j < tileMapHeight; j++) {
    			if (tileMap[i][j] != 0 ) {
    				batch.draw(tempTile, getIsoX(j,i), (getIsoY(j,i) + 16));	
    			}
    		}
    	}
    }
	
	// Get the isometric projection coordinate X, given tilemap X and Y as parameters.
    private int getIsoX(int mapx, int mapy) {
    	return (screenWidth - ((tileMapWidth * 32 - 32) + (mapy - mapx) * tileWidthHalf));
    			
    }

	// Get the isometric projection coordinate Y, given tilemap X and Y as parameters.
    private int getIsoY(int mapx, int mapy) {
    	return (screenHeight - ((tileMapHeight * 16 - 32) + (mapy + mapx) * tileHeightHalf));
    			
    }

	
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
	    		System.err.println("Warning: invalid direction of character!"); //FIXME: written 60 times per second
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
        		System.err.println("Warning: invalid direction of character!"); //FIXME: written 60 times per second
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
        		System.err.println("Warning: invalid direction of character!"); //FIXME: written 60 times per second
        	}
    		break;
    	default:
    		System.err.println("Warning: invalid animation frame!"); //FIXME: written 60 times per second
    			
    	}
    	

    	
    }    
	@Override
	public void dispose() {
		batch.dispose();
		tempTile.dispose();
		spriteSheet.dispose();
		font.dispose();
	}

	
}
