package com.github.eaksi.stactics.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.github.eaksi.stactics.engine.SolGDX.TempDirection;

public class Keyboard {
	protected static void getInput(SolGDX sol) {

		// Quit the application
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))		Gdx.app.exit();

        // Moving the view
		if (Gdx.input.isKeyPressed(Keys.A))	sol.camera.moveLeft();
		if (Gdx.input.isKeyPressed(Keys.D))	sol.camera.moveRight();
		if (Gdx.input.isKeyPressed(Keys.S))	sol.camera.moveDown();
		if (Gdx.input.isKeyPressed(Keys.W))	sol.camera.moveUp();
		
		// Zooming the view
		if (Gdx.input.isKeyPressed(Keys.Z)) {
				sol.camera.setZoom(true);
		} else if (Gdx.input.isKeyPressed(Keys.X)) {
				sol.camera.setZoom(false);
		}
		
        // Rotating the camera (disabled)    
        //if (Gdx.input.isKeyPressed(Keys.Q))			camera.rotate(-1f, 0, 0, 1);
        //if (Gdx.input.isKeyPressed(Keys.E))			camera.rotate(1f, 0, 0, 1);
       
        
		if (sol.chAnimating)	// if animation in progress, break 
			return;
		
		if(Gdx.input.isKeyPressed(Keys.UP)) {
			sol.setMoveDirection(TempDirection.NE);
			return;
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)) {
			sol.setMoveDirection(TempDirection.SW);
			return;
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			sol.setMoveDirection(TempDirection.NW);
			return;
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
			sol.setMoveDirection(TempDirection.SE);
			return;
		}
	    
	}
}
