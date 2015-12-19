package com.github.eaksi.stactics.engine.gfx;

/*
 * 	All drawables in isometric engine inherit this class
 */
public class Drawable implements Comparable<Drawable>{
	
	private static int runningId = 0;
	
	protected int id;			// id inside animation framework

	public int isoX, isoY;		// used by graphics engine
	public int tileX, tileY;	// used by AI, graphics engine
	public int z;			// TODO: not used for anything yet



	public Drawable() {
		id = ++runningId;	// generates unique id
		
	}


	public int getId()		 { return id; }

	public int getZ()		 { return z; }
	public void setZ(int z)	 { this.z = z; }


	@Override
	public int compareTo(Drawable d) {
		return (Integer.valueOf(z)).compareTo(Integer.valueOf(d.getZ()));
	}
}
