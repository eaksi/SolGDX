package com.github.eaksi.stactics.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

public class World {

	private static Hashtable<Integer, Skill> skills; // id, name
	private static boolean skillTableInitialized = false;
	private static Vector<String> fNameList;
	private static Vector<String> lNameList;

	// Initialize everything
	public static void initialize() {
		RNG.initialize();
		initializeNameGenerator();
		initializeSkills();
		// initializeItems();
	}

	private static void initializeNameGenerator() {
		fNameList = loadNameFile("data/victorianenglish_f.txt");
		lNameList = loadNameFile("data/dragonnames.txt");

		System.out.println("World: NameGen initialized (" + (fNameList.size() + lNameList.size()) + " names loaded)");
		// /*DEBUG*/ for (String s: lNameList) System.out.println(s);
	}

	private static Vector<String> loadNameFile(String fileName) {
		Vector<String> names = new Vector<String>();

		String line = null;
		try (BufferedReader filein = new BufferedReader(new FileReader(fileName))) {
			line = filein.readLine();
			while (line != null) {
				if (!line.equals(""))
					names.addElement(line);
				line = filein.readLine();
			}
		} catch (FileNotFoundException fnf) {
			System.err.println("ERROR: FileNotFoundException in NameGen!");
		} catch (IOException ioe) {
			System.err.println("ERROR: IOException in NameGen!");
		}

		return names;
	}

	public static String getRandomFName() {
		int nr = RNG.nextInt(fNameList.size());
		return fNameList.get(nr);
	}

	public static String getRandomLName() {
		int nr = RNG.nextInt(lNameList.size());
		return lNameList.get(nr);
	}

	private static void initializeSkills() { // TODO: load from file
		if (!skillTableInitialized) {
			skillTableInitialized = true;

			skills = new Hashtable<Integer, Skill>();

			skills.put(1, new Skill(1, "Swords", 1, 5));
			// rest of the skills
			System.out.println("World: Skills initialized");
		} else {
			System.err.println("WARNING: World: Skills already initialized!");
		}

	}

	public static String getSkillName(int id) {
		return skills.get(id).getName();
	}

}
