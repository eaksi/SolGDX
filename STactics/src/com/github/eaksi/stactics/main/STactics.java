package com.github.eaksi.stactics.main;

import java.util.Vector;

import com.github.eaksi.stactics.db.Creature;
import com.github.eaksi.stactics.db.World;
import com.github.eaksi.stactics.engine.gfx.Entity;

/**
 * Mostly a class for prototyping game model things (for now)
 */
public class STactics {
	
	// TODO: Make an editor using Swing (or preferably a GUI editor)
	// TODO: Character level up / skill choices, make tests

	public static void main(String[] args) {

		World.initialize();
		
		Vector<Entity> battlers = new Vector<Entity>();
		for (int i=0; i<4; i++) {
			battlers.add(new Entity(new Creature(),0,0));
		}

		// test delay system
		for (int i = 0; i < 50; i++) {
			System.out.println("*** TURN "+ (i+1) +" ***");
			for (Entity battler: battlers) {
				battler.cr.isReady();
			}
		}
		

		
	}

}
