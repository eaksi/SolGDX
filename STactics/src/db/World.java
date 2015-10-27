package db;

import java.util.Hashtable;

public class World {
	
	private static Hashtable<Integer,Skill> skills;  // id, name
	private static boolean skillTableInitialized = false;
	
	// Initialize everything
	public static void initialize() {
		initializeSkills();
		//initializeItems();		
	}
	
	private static void initializeSkills() {  // TODO: load from file
		if (!skillTableInitialized) {
			skillTableInitialized = true;
			
			skills = new Hashtable<Integer,Skill>();
			
			skills.put(1, new Skill(1,"Swords",1,5));
			// rest of the skills
		} else {
			System.err.println("WARNING: Skills already initialized!");
		}
		
	}

	public static String getSkillName(int id) {
		return skills.get(id).getName();
	}
	

		
}

