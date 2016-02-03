package com.github.eaksi.stactics.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.github.eaksi.stactics.engine.gfx.Actor.Direction;

/**
 * This class handles the keyboard and mouse controls. Called from render/update
 * loop in SolGDX.
 */
public class Controls {

	protected static void handleInput(SolGDX sol) {

		// Quit the application
		if (Gdx.input.isKeyPressed(Keys.ESCAPE))
			Gdx.app.exit();

		// Move the camera view
		if (Gdx.input.isKeyPressed(Keys.A))
			sol.camera.moveLeft();
		if (Gdx.input.isKeyPressed(Keys.D))
			sol.camera.moveRight();
		if (Gdx.input.isKeyPressed(Keys.S))
			sol.camera.moveDown();
		if (Gdx.input.isKeyPressed(Keys.W))
			sol.camera.moveUp();

		// Zoom the view
		if (Gdx.input.isKeyJustPressed(Keys.Z)) {
			sol.camera.setZoom(true);
		} else if (Gdx.input.isKeyJustPressed(Keys.X)) {
			sol.camera.setZoom(false);
		}

		// XXX: temp, test mouse input, click/drag
		if (Gdx.input.isButtonPressed(Buttons.LEFT) && Gdx.input.justTouched()) {
			sol.mouseScreenX = Gdx.input.getX();
			sol.mouseScreenY = Gdx.input.getY();
		} else if (Gdx.input.isTouched()) {
			sol.mouseDragX = Gdx.input.getX();
			sol.mouseDragY = Gdx.input.getY();
		}

		// displays info on selected creature
		if (Gdx.input.isKeyJustPressed(Keys.HOME))
			sol.showEntityInfo = !sol.showEntityInfo;

		// refreshes action points and moves
		if (Gdx.input.isKeyJustPressed(Keys.END))
			sol.actors.get(sol.nr).cr.startNewTurn();

		// Toggle debug mode
		if (Gdx.input.isKeyJustPressed(Keys.NUM_0))
			sol.debug = !sol.debug;

		// Toggle various drawing debug flags
		if (Gdx.input.isKeyJustPressed(Keys.NUM_7))
			sol.tileCoordDebug = !sol.tileCoordDebug;
		if (Gdx.input.isKeyJustPressed(Keys.NUM_8))
			sol.tileHeightDebug = !sol.tileHeightDebug;
		if (Gdx.input.isKeyJustPressed(Keys.NUM_9))
			sol.drawOrderDebug = !sol.drawOrderDebug;

		// Rotating the camera (disabled)
		// if (Gdx.input.isKeyPressed(Keys.Q)) camera.rotate(-1f, 0, 0, 1);
		// if (Gdx.input.isKeyPressed(Keys.E)) camera.rotate(1f, 0, 0, 1);

		if (sol.chAnimating) // if animation in progress, break
			return;

		// current character movement controls
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			sol.setMoveDirection(Direction.NE);
			return;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			sol.setMoveDirection(Direction.SW);
			return;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			sol.setMoveDirection(Direction.NW);
			return;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			sol.setMoveDirection(Direction.SE);
			return;
		}

		// Change controllable entity id
		if (Gdx.input.isKeyJustPressed(Keys.PAGE_DOWN)) {
			if (sol.actors.size() != (sol.nr + 1)) {
				sol.nr++;
			} else {
				sol.nr = 0;
			}
			return;
		} else if (Gdx.input.isKeyJustPressed(Keys.PAGE_UP)) {
			if (sol.nr <= 0) {
				sol.nr = sol.actors.size() - 1;
			} else {
				sol.nr--;
			}
			return;
		}
	}

}
