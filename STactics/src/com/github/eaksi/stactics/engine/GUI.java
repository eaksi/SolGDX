package com.github.eaksi.stactics.engine;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

public class GUI {		//FIXME: refactor!!!
    
	private GlyphLayout layout = new GlyphLayout();	// XXX: temp
	
	protected static void draw(SolGDX sol, SpriteBatch guiBatch) {
      
		/*// XXX: quick hack for white text outline
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
     	
    	sol.font.setColor(0.6f, 0.6f, 0.6f, 1f);
    	sol.font.draw(guiBatch, "name: "+ sol.entities.get(sol.nr).cr.getFullName(), 10, sol.screenHeight-10);
     	
     	// debug info
    	if (sol.debugFlag) {
    		sol.smallFont.setColor(0f, 0f, 0f, 1f);
    		
    		sol.smallFont.draw(guiBatch, "creature id: "+ sol.entities.get(sol.nr).cr.getId(), 10, sol.screenHeight-15, sol.screenWidth-150, Align.right, true);
    		sol.smallFont.draw(guiBatch, "entity id: "+ sol.entities.get(sol.nr).getId(), 10, sol.screenHeight-30, sol.screenWidth-150, Align.right, true);
    		
    		sol.smallFont.draw(guiBatch, "framesLeft: "+ sol.entities.get(sol.nr).getFramesLeft(), 10, sol.screenHeight-15, sol.screenWidth-20, Align.right, true);
    		sol.smallFont.draw(guiBatch, "animation: "+ sol.entities.get(sol.nr).getAnimString(), 10, sol.screenHeight-30, sol.screenWidth-20, Align.right, true);
    		sol.smallFont.draw(guiBatch, "entity.isoX: "+sol.entities.get(sol.nr).isoX, 10, sol.screenHeight-45, sol.screenWidth-20, Align.right, true);
    		sol.smallFont.draw(guiBatch, "entity.isoY: "+sol.entities.get(sol.nr).isoY, 10, sol.screenHeight-60, sol.screenWidth-20, Align.right, true);
    		sol.smallFont.draw(guiBatch, "entity.tileX: "+sol.entities.get(sol.nr).tileX, 10, sol.screenHeight-75, sol.screenWidth-20, Align.right, true);
    		sol.smallFont.draw(guiBatch, "entity.tileY: "+sol.entities.get(sol.nr).tileY, 10, sol.screenHeight-90, sol.screenWidth-20, Align.right, true);

    	} else {
    		sol.smallFont.setColor(0f, 0f, 0f, 1f);
    		sol.smallFont.draw(guiBatch, "press '0' for debug mode, '9' for draw order", 10, sol.screenHeight-15, sol.screenWidth-20, Align.right, true);
    	}

    	//layout.setText(font, "layout test");
    	//font.draw(guiBatch, layout, 200 + layout.width / 3, 200 + layout.height / 3);


    }

}

