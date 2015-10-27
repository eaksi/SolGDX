import db.Creature;
import db.World;
import tools.RulesLoader;

public class SolTactics {

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
