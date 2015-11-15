package com.github.eaksi.stactics.main;

import java.util.Vector;

import com.github.eaksi.stactics.db.Creature;
import com.github.eaksi.stactics.db.World;

public class STactics {
	
	public static void main(String[] args) {

		World.initialize();
		
		Vector<Creature> npcs = new Vector<Creature>();
		npcs.add(new Creature());
		npcs.add(new Creature());
		npcs.add(new Creature());
		npcs.add(new Creature());
		npcs.add(new Creature());

		
		// test delay system
		for (int i = 0; i < 50; i++) {
			System.out.println("*** TURN "+ (i+1) +" ***");
			for (Creature npc: npcs) {
				npc.isReady();
			}
		}
		

		
	}

}
