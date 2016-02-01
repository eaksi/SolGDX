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

		Gfx.getFont().setColor(0.6f, 0.6f, 0.6f, 1f);
		Gfx.getSmallFont().setColor(0.6f, 0.6f, 0.6f, 1f);
		
		// display current character info
		Gfx.getFont().draw(guiBatch, "name: " + sol.actors.get(sol.nr).cr.getFullName(), 10, sol.screenHeight - 10);
		Gfx.getSmallFont().draw(guiBatch, "id: " + sol.actors.get(sol.nr).cr.getId(), 10, sol.screenHeight - 30);
		Gfx.getSmallFont().draw(guiBatch, "hp: " + sol.actors.get(sol.nr).cr.getHP() + "/" + 
				sol.actors.get(sol.nr).cr.getHPMax() , 10, sol.screenHeight - 45);
		Gfx.getSmallFont().draw(guiBatch, sol.actors.get(sol.nr).cr.getMAString(), 10, sol.screenHeight - 60);

		leftDebugY = 0;
		rightDebugY = 0;

		Gfx.getSmallFont().setColor(0f, 0f, 0f, 1f);

		// display debug info
		if (sol.debug) {

			// left side of debug
			debugDraw(sol, guiBatch, "creature id: " + sol.actors.get(sol.nr).cr.getId(), true);
			debugDraw(sol, guiBatch, "entity id: " + sol.actors.get(sol.nr).getId(), true);

			// right side of debug
			debugDraw(sol, guiBatch, "anim. frame nr.: " + sol.actors.get(sol.nr).getCurrentFrame(), false);
			debugDraw(sol, guiBatch, "animation: " + sol.actors.get(sol.nr).getAnimString(), false);
			debugDraw(sol, guiBatch, "heading: " + sol.actors.get(sol.nr).getHeading(), false);
			debugDraw(sol, guiBatch, "entity.isoX: " + sol.actors.get(sol.nr).isoX, false);
			debugDraw(sol, guiBatch, "entity.isoY: " + sol.actors.get(sol.nr).isoY, false);
			debugDraw(sol, guiBatch, "entity.z: " + sol.actors.get(sol.nr).z, false);
			debugDraw(sol, guiBatch, "entity.tileX: " + sol.actors.get(sol.nr).tileX, false);
			debugDraw(sol, guiBatch, "entity.tileY: " + sol.actors.get(sol.nr).tileY, false);
			rightDebugY += 15;
			debugDraw(sol, guiBatch, "mouseScreenX: " + sol.mouseScreenX, false);
			debugDraw(sol, guiBatch, "mouseScreenY: " + sol.mouseScreenY, false);
			debugDraw(sol, guiBatch, "Dragged X: " + (sol.mouseDragX - sol.mouseScreenX), false);
			debugDraw(sol, guiBatch, "Dragged Y: " + (sol.mouseDragY - sol.mouseScreenY), false);
			//debugDraw(sol, guiBatch, "mouseIsoX: " + sol.mouseIsoX, false);
			//debugDraw(sol, guiBatch, "mouseIsoY: " + sol.mouseIsoY, false);

		} else {  // display other info
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
			Gfx.getSmallFont().draw(guiBatch, text, 10, sol.screenHeight - leftDebugY, sol.screenWidth - 150, Align.right,
					true);
		} else {
			rightDebugY += 15;
			Gfx.getSmallFont().draw(guiBatch, text, 10, sol.screenHeight - rightDebugY, sol.screenWidth - 20, Align.right,
					true);
		}

	}

}
