package com.github.eaksi.stactics.main;

import com.github.eaksi.stactics.db.Creature;
import com.github.eaksi.stactics.db.World;
import com.github.eaksi.stactics.tools.RulesLoader;

public class STactics {

	public static void main(String[] args) {

		//RulesLoader.tempLoad();
		//RulesLoader.tempSave();

		World.initialize();
		
		Creature npc = new Creature();
		System.out.println("Name: "+ npc.getFullName());
		System.out.println("Skill id:1 level = " + World.getSkillName(1) + ": " + npc.skills.get(1));

		// test delay system
		for (int i = 0; i < 50; i++) {
			System.out.println("*** TURN "+ i +" ***");
			npc.isReady();
		}
		

		
	}

}
