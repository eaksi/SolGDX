package com.github.eaksi.solgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class SolGDX extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch; //gittest
	private BitmapFont font;
	private Texture tempImg;
	private Texture tempTile;
	private Rectangle tempRect;
	private final int screenWidth = 640;
	private final int screenHeight = 400;
	
	private boolean animating = false;
	private int movingDirection = 0;    // 1 = NE, 3 = SE, 5 = SW, 7 = NW
	private int moveFrame = -1;
	
	private int tileWidth = 64;
	private int tileHeight = 32;
	private int tileWidthHalf = tileWidth / 2; 		// slight optimization
	private int tileHeightHalf = tileHeight / 2; 	// slight optimization
	
	private int tileMap[][] = {	{1,1,1,1,1,1},
								{1,0,0,0,1,1},
								{1,0,0,1,1,1},
								{1,0,0,1,1,1},
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
		tempImg = new Texture("cleric_placeholder.png");
		tempTile = new Texture("64px_tile_placeholder.png");
		font = new BitmapFont();
        font.setColor(Color.BLACK);
        
        tempRect = new Rectangle();
        tempRect.x = screenWidth - (tileMapWidth * 32 - 32); // TODO: better formulas for OpenGL/LibGDX/Sprite/tiling
        tempRect.y = screenHeight - (tileMapHeight * 16 - 20 ); // TODO: better formulas for OpenGL/LibGDX/Sprite/tiling
        tempRect.width = 64;
        tempRect.height = 64;
        
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
	    batch.end();
		
	    // update FPS counter on window title
	    Gdx.graphics.setTitle("SolGDX     FPS: " + Gdx.graphics.getFramesPerSecond());
				
		getKeyboardInputs();	// keyboard controls handled at this point

		moveTempRect(); 		// XXX: temp character turn-based keyboard movement
	}
	
	public void getKeyboardInputs() {	// simplified, press multiple buttons at once etc.

		if (animating) return;
				
	    if(Gdx.input.isKeyPressed(Keys.UP)) {
	    	movingDirection = 1;
	    	moveFrame = 0;
	    	animating = true;
	    }

	    if(Gdx.input.isKeyPressed(Keys.DOWN)) {
	    	movingDirection = 5;
	    	moveFrame = 0;
	    	animating = true;
	    }
	    
	    if(Gdx.input.isKeyPressed(Keys.LEFT)) {
	    	movingDirection = 7;
	    	moveFrame = 0;
	    	animating = true;
		}
	    
	    if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
	    	movingDirection = 3;
	    	moveFrame = 0;
	    	animating = true;
	    }
	    
	    if(Gdx.input.isKeyPressed(Keys.Q)) { // (Q)uit
	    	Gdx.app.exit();
	    }

	}
	

	//move rectangle/sprite
	private void moveTempRect() {
		
		if (animating) {
			switch (movingDirection) {
			case 1:
		    	tempRect.x += 2;
		    	tempRect.y += 1;
				break;
			case 3:
		    	tempRect.x += 2;
		    	tempRect.y -= 1;
				break;
			case 5:
		    	tempRect.x -= 2;
		    	tempRect.y -= 1;
				break;
			case 7:
		    	tempRect.x -= 2;
		    	tempRect.y += 1;
				break;
			default:
				break;
			}

			moveFrame++;
			
			if (moveFrame >= 16) {
				moveFrame = -1;
				animating = false;
			}
		}
	}
	
	
	private void drawTiles() {

    	int tempx = 0, tempy = 0;
    	
    	
    	// FIXME: make comments and better code for transforming coordinates
    	for (int i = 0; i < tileMapWidth; i++) {
        	for (int j = 0; j < tileMapHeight; j++) {
    			if (tileMap[i][j] != 0 ) {
    				tempx = screenWidth - ((tileMapWidth * 32 - 32) + (i - j)* tileWidthHalf);
    				tempy = screenHeight - ((tileMapHeight * 16 - 32) + (i + j) * tileHeightHalf);
    				batch.draw(tempTile, tempx, tempy);	
    			}
    		}
    	}
    }
	
    
    private void drawCharacters() {
    	batch.draw(tempImg, tempRect.x, tempRect.y);
    }
    
    
	@Override
	public void dispose() {
		batch.dispose();
		tempImg.dispose();
		tempTile.dispose();
		font.dispose();
	}

	
}
