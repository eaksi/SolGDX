package com.github.eaksi.stactics.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.github.eaksi.stactics.engine.gfx.Entity.Direction;

public class Controls {
	
	protected static void getInput(SolGDX sol) {

		// Quit the application
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))		Gdx.app.exit();

        // Move the camera view
		if (Gdx.input.isKeyPressed(Keys.A))	sol.camera.moveLeft();
		if (Gdx.input.isKeyPressed(Keys.D))	sol.camera.moveRight();
		if (Gdx.input.isKeyPressed(Keys.S))	sol.camera.moveDown();
		if (Gdx.input.isKeyPressed(Keys.W))	sol.camera.moveUp();
		
		// Zoom the view
		if (Gdx.input.isKeyJustPressed(Keys.Z)) {
				sol.camera.setZoom(true);
		} else if (Gdx.input.isKeyJustPressed(Keys.X)) {
				sol.camera.setZoom(false);
		}
		
		// Toggle debug mode
		if (Gdx.input.isKeyJustPressed(Keys.NUM_0))	sol.debug = !sol.debug;
		if (Gdx.input.isKeyJustPressed(Keys.NUM_9)) sol.drawOrderFlag = !sol.drawOrderFlag;
		
        // Rotating the camera (disabled)    
        //if (Gdx.input.isKeyPressed(Keys.Q))			camera.rotate(-1f, 0, 0, 1);
        //if (Gdx.input.isKeyPressed(Keys.E))			camera.rotate(1f, 0, 0, 1);
       
        
		if (sol.chAnimating)	// if animation in progress, break 
			return;
		
		// current character movement controls
		if(Gdx.input.isKeyPressed(Keys.UP)) {
			sol.setMoveDirection(Direction.NE);
			return;
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)) {
			sol.setMoveDirection(Direction.SW);
			return;
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			sol.setMoveDirection(Direction.NW);
			return;
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
			sol.setMoveDirection(Direction.SE);
			return;
		}
	    
		// Change controllable entity id
		if (Gdx.input.isKeyJustPressed(Keys.PAGE_DOWN)) {
			if (sol.entities.size() != (sol.nr+1)) {
				sol.nr++;
			} else {
				sol.nr = 0;
			}
			return;
		} else if (Gdx.input.isKeyJustPressed(Keys.PAGE_UP)) {
			if (sol.nr <= 0) {
				sol.nr = sol.entities.size()-1;
			} else {
				sol.nr--;
			}
			return;
		}
	}

}
