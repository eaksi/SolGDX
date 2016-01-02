package com.github.eaksi.stactics.engine.gfx;

import java.util.concurrent.atomic.AtomicInteger;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/*
 * 	All drawables in isometric engine inherit this class
 */
public abstract class Drawable implements Comparable<Drawable>{
	
	private static AtomicInteger runningId = new AtomicInteger();
	
	protected int id;			// id inside animation framework

	public int isoX, isoY;		// used by graphics engine
	public int tileX, tileY;	// used by AI, graphics engine
	public int z;				// for draw order

	public Drawable() {
		id = runningId.incrementAndGet();	// generates unique id
	}

	public int getId()		 { return id; }

	public int getZ()		 { return z; }
	public void setZ(int z)	 { this.z = z; }

//	public void draw(int isox, int isoy);

	public abstract TextureRegion getSprite();
	
	@Override
	public int compareTo(Drawable d) {
		return (Integer.valueOf(z)).compareTo(Integer.valueOf(d.getZ()));
	}
}
