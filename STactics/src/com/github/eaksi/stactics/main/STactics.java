package com.github.eaksi.stactics.main;

import com.github.eaksi.stactics.db.Creature;
import com.github.eaksi.stactics.db.World;
import com.github.eaksi.stactics.tools.RulesLoader;

public class STactics {

	public static void main(String[] args) {

		RulesLoader.tempLoad();
		RulesLoader.tempSave();

		World.initialize();
		
		Creature npc = new Creature();
		System.out.println("Name: "+ npc.getFullName());
	/*	System.out.println("ma : "+ npc.getMeleeAttack());
		System.out.println("ra : "+ npc.getRangedAttack());
		System.out.println("mga: "+ npc.getMagicAttack());
		System.out.println("pd : "+ npc.getPhysicalDefense());
		System.out.println("md : "+ npc.getMagicDefense());*/
		System.out.println("Skill id:1 level = " + World.getSkillName(1) + ": " + npc.skills.get(1));
		
		

		
	}

}
