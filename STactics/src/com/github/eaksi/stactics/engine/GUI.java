package com.github.eaksi.stactics.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

/**
 * This class draws everything on the guiPatch (SpriteBatch).
 * The contents do not move with the camera, unlike the contents of the other batch.
 */
public class GUI {

	private static int leftDebugY = 0;
	private static int rightDebugY = 0;

	/**
	 *  Called from SolGDX on every render() cycle after everything else has been rendered.
	 */
	protected static void draw(SolGDX sol, SpriteBatch guiBatch) {

		sol.font.setColor(0.6f, 0.6f, 0.6f, 1f);
		
		// display current character info
		sol.font.draw(guiBatch, "name: " + sol.entities.get(sol.nr).cr.getFullName(), 10, sol.screenHeight - 10);

		leftDebugY = 0;
		rightDebugY = 0;

		sol.smallFont.setColor(0f, 0f, 0f, 1f);

		// debug info
		if (sol.debug) {

			// left side of debug
			debugDraw(sol, guiBatch, "creature id: " + sol.entities.get(sol.nr).cr.getId(), true);
			debugDraw(sol, guiBatch, "entity id: " + sol.entities.get(sol.nr).getId(), true);

			// right side of debug
			debugDraw(sol, guiBatch, "anim. frame: " + sol.entities.get(sol.nr).getCurrentFrame(), false);
			debugDraw(sol, guiBatch, "animation: " + sol.entities.get(sol.nr).getAnimString(), false);
			debugDraw(sol, guiBatch, "heading: " + sol.entities.get(sol.nr).getHeading(), false);
			debugDraw(sol, guiBatch, "entity.isoX: " + sol.entities.get(sol.nr).isoX, false);
			debugDraw(sol, guiBatch, "entity.isoY: " + sol.entities.get(sol.nr).isoY, false);
			debugDraw(sol, guiBatch, "entity.z: " + sol.entities.get(sol.nr).z, false);
			debugDraw(sol, guiBatch, "entity.tileX: " + sol.entities.get(sol.nr).tileX, false);
			debugDraw(sol, guiBatch, "entity.tileY: " + sol.entities.get(sol.nr).tileY, false);
			rightDebugY += 15;
			debugDraw(sol, guiBatch, "mouseScreenX: " + sol.mouseScreenX, false);
			debugDraw(sol, guiBatch, "mouseScreenY: " + sol.mouseScreenY, false);
			debugDraw(sol, guiBatch, "Dragged X: " + (sol.mouseDragX - sol.mouseScreenX), false);
			debugDraw(sol, guiBatch, "Dragged Y: " + (sol.mouseDragY - sol.mouseScreenY), false);
			debugDraw(sol, guiBatch, "mouseIsoX: " + sol.mouseIsoX, false);
			debugDraw(sol, guiBatch, "mouseIsoY: " + sol.mouseIsoY, false);

		} else {
			debugDraw(sol, guiBatch, "Help:", false);
			debugDraw(sol, guiBatch, "PGUP & PGDOWN = change entity", false);
			debugDraw(sol, guiBatch, "HOME = display battler info", false);
			debugDraw(sol, guiBatch, "END = do \"new turn\" things with battler", false);
			debugDraw(sol, guiBatch, "9 = toggle z draw order", false);
			debugDraw(sol, guiBatch, "0 = toggle debug info", false);
		}

	}

	/**
	 *	Helper method for draw(...), makes for cleaner code 
	 */
	private static void debugDraw(SolGDX sol, SpriteBatch guiBatch, String text, boolean leftAlign) {
		if (leftAlign) {
			leftDebugY += 15;
			sol.smallFont.draw(guiBatch, text, 10, sol.screenHeight - leftDebugY, sol.screenWidth - 150, Align.right,
					true);
		} else {
			rightDebugY += 15;
			sol.smallFont.draw(guiBatch, text, 10, sol.screenHeight - rightDebugY, sol.screenWidth - 20, Align.right,
					true);
		}

	}

}
