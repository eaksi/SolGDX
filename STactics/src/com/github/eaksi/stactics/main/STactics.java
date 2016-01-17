package com.github.eaksi.stactics.main;

import java.util.Vector;

import com.github.eaksi.stactics.db.Creature;
import com.github.eaksi.stactics.db.World;
import com.github.eaksi.stactics.engine.gfx.Entity;

public class STactics {
	
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
