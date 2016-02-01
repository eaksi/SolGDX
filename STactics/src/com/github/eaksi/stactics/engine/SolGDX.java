package com.github.eaksi.stactics.engine;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.eaksi.stactics.db.AreaMap;
import com.github.eaksi.stactics.db.Creature;
import com.github.eaksi.stactics.engine.gfx.Drawable;
import com.github.eaksi.stactics.engine.gfx.Actor;
import com.github.eaksi.stactics.engine.gfx.Actor.Direction;
import com.github.eaksi.stactics.engine.gfx.Tile;

/**
 * SolGDX is the coordinating class for the graphics engine.
 *
 */
public class SolGDX extends ApplicationAdapter {

	// FIXME: aspect ratio change on window size change 
	
	// debug flags and related things
	boolean debug = false;
	boolean drawOrderDebug = false;
	private int drawOrder = 0;
	boolean autoRefreshMovesDebug = true;
	boolean hitBattlersDebug = true;
	
	boolean showEntityInfo = false;
	
	Camera camera;
	private SpriteBatch batch;		// primary SpriteBatch for graphics
	private SpriteBatch guiBatch;	// GUI SpriteBatch, does not move with camera

	final int screenWidth = 1024; // screen resolution
	final int screenHeight = 800;

	// if the engine is animating movement -> move keys disabled
	boolean chAnimating = false; 

	private int tileWidth = 64; 	// width of tile graphics in pixels
	private int tileHeight = 32;	// height of tile graphics in pixels

	private AreaMap areaMap;

	Vector<Actor> actors;		// contains the actors currently in game
	int nr = 0; 					// current entity Number

	Vector<Drawable> painter;		// has all the drawable tiles and sprites in order

	// debug
	public int mouseScreenX = -999;
	public int mouseScreenY = -999;
	public int mouseIsoX = -999;
	public int mouseIsoY = -999;
	public int mouseDragX = -999;
	public int mouseDragY = -999;

	@Override
	public void create() {

		areaMap = new AreaMap();
		actors = new Vector<Actor>();
		actors.add(new Actor(new Creature(), 1, 4));
		actors.add(new Actor(new Creature(), 4, 5));
		actors.add(new Actor(new Creature(), 6, 4));

		System.out.println("***ENTITIES***");
		for (Actor e : actors) {
			System.out.println(e.cr.getId() + ": " + e.cr.getFullName());
		}
		System.out.println("**************");


		// setup camera
		camera = new Camera(screenWidth, screenHeight);

		batch = new SpriteBatch();
		guiBatch = new SpriteBatch();

		Gfx.initialize(); // initialize Gfx class, load graphics etc.

		setEntityLocations(); // initial setup of entity locations

		initializePainter(); // initialize painter's algorithm (make & sort Drawables)

		/* print the AreaMap width and height and w/h difference
		 *  (negative = more width than height)	 */
		if (debug) {
			System.out.println("areaMap.getWidth() = " + areaMap.getWidth() + "  areaMap.getHeight() = "
					+ areaMap.getHeight() + "  difference = " + (areaMap.getWidth() - areaMap.getHeight()));
		}

	}

	@Override
	public void render() {


		Gdx.gl.glClearColor(1, 1, 1, 1); 					// white screen
		//Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1); 		// light grey screen
		//Gdx.gl.glClearColor(0, 0, 0, 1);			 		// black screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		Controls.handleInput(this); // keyboard controls handled at this point
		
		// update animation frames for actors
		for (Actor e : actors) {
			e.updateAnimation();
		}
		
		chAnimating = actors.get(nr).getAnimation() != Actor.Animation.IDLE; // if no animation, can move
		
		batch.setProjectionMatrix(camera.combined);

		camera.updateZoom();

		// draw everything
		batch.begin();
		drawBattleMap();
		batch.end();

		guiBatch.begin();
		GUI.draw(this, guiBatch); // draw GUI and possible debug data
		guiBatch.end();

		// update FPS counter on window title
		Gdx.graphics
				.setTitle("SolGDX     FPS: " + Gdx.graphics.getFramesPerSecond() + " Zoom: " + camera.getZoomLevel());

	}

	private void setEntityLocations() {
		// set starting locations of actors
		for (Actor e : actors) {
			e.isoX = toIsoX(e.tileY, e.tileX) + 16;
			e.isoY = toIsoY(e.tileY, e.tileX) + 22; // XXX: sprite/tile sizes hack
			
		}
	}

	private void initializePainter() {

		painter = new Vector<Drawable>();
		Tile tile;

		// add tiles
		for (int i = 0; i < areaMap.getWidth(); i++) {
			for (int j = 0; j < areaMap.getHeight(); j++) {
				if (areaMap.getTile(i, j) == 0) {
					tile = new Tile(i, j, toIsoX(j, i), toIsoY(j, i), toIsoY(j, i), true);
				} else {
					tile = new Tile(i, j, toIsoX(j, i), toIsoY(j, i) + areaMap.getTile(i, j) * 16, toIsoY(j, i),
							false);
				}

				painter.add((Drawable) tile);

				// debug
				if (debug)
					System.out.println("draw tile: (" + tile.tileX + "," + tile.tileY + ") z-depth: " + tile.getZ()
							+ " height: " + areaMap.getTile(i, j) + " water: " + tile.isWater());
			}
		}

		// add characters
		for (Actor e : actors) {
			// XXX: z position guessed (but tested, need frame by frame
			// analysis, height changes testing and gfx)
			e.setZ(e.isoY - 30);	// should be correct (for now)
			//e.setZ(e.isoY - 27);	// minor clipping on floor tiles when moving?
			painter.add(e);
		}

		// initial sorting
		Collections.sort((List<Drawable>) painter, Collections.reverseOrder());

	}

	// check if creature can move to a tile, this fires only once per move
	// XXX: refactor whole method
	void setMoveDirection(Direction d) {

		int tryX = 0;
		int tryY = 0;

		actors.get(nr).setHeading(d);

		switch (actors.get(nr).getHeading()) {
		case NE:
			tryX = -1;
			break;
		case SE:
			tryY = 1;
			break;
		case SW:
			tryX = 1;
			break;
		case NW:
			tryY = -1;
			break;
		default:
			break;
		}

		// test if able to move in terrain
		if (areaMap.getTile((actors.get(nr).tileX + tryX), actors.get(nr).tileY + tryY) != 1) {
			printMoveDebug("Can't move (terrain): " + actors.get(nr).getHeading(), tryX, tryY);
			return;
		}

		// test if able to move because of other actors
		for (Actor e : actors) {
			if (actors.get(nr).tileX + tryX == e.tileX && actors.get(nr).tileY + tryY == e.tileY) {
				if (!hitBattlersDebug) {
					printMoveDebug("Can't move, " + e.cr.getName() + " (id:" + e.cr.getId() + ") is in the way: "
							+ actors.get(nr).getHeading(), tryX, tryY);
				} else if (actors.get(nr).cr.getActions() > 0) { // XXX: TEMP attack code
					actors.get(nr).animFrameNr = 0;
					actors.get(nr).setAnimation(Actor.Animation.ATTACK);
					chAnimating = true;

					actors.get(nr).cr.spendAction();	
					
					// animate victim
					e.cr.applyDamage(1);
					e.animFrameNr = 0;
					e.setAnimation(Actor.Animation.HURT);
				} else {
					System.out.println(actors.get(nr).cr.getName() + " ran out of actions!");
				}
				return; // NOTE!: "return;" here
			}
		}

		// refresh moves automatically if debug flag for that is on
		if (actors.get(nr).cr.getMoves() <= 0 && autoRefreshMovesDebug) actors.get(nr).cr.startNewTurn(); 
		
		
		// check if entity has moves left
		// TODO: moves where more than 1 point of move needed
		if (actors.get(nr).cr.getMoves() > 0) {
			
			printMoveDebug("" + actors.get(nr).getHeading(), tryX, tryY);
			actors.get(nr).tileX += tryX;
			actors.get(nr).tileY += tryY;
	
			// if everything okay, set movement in motion
			actors.get(nr).animFrameNr = 0;
			actors.get(nr).setAnimation(Actor.Animation.WALK);
			chAnimating = true;
			
			actors.get(nr).cr.spendMove();
			

		} else {
			System.out.println(actors.get(nr).cr.getName() + " ran out of moves!");
		}
	}

	private void printMoveDebug(String dir, int x, int y) {
		System.out.println(dir + "(" + actors.get(nr).tileX + "," + actors.get(nr).tileY + ") -> ("
				+ (actors.get(nr).tileX + x) + "," + (actors.get(nr).tileY + y) + ")");
	}

	/**
	 *  Get the isometric projection coordinate X, given tilemap X and Y as parameters
	 */
	private int toIsoX(int mapx, int mapy) {
		return (screenWidth - ((areaMap.getWidth() * 32) + (mapy - mapx) * (tileWidth / 2)));
	}

	/**
	 *  Get the isometric projection coordinate Y, given tilemap X and Y as parameters
	 */
	private int toIsoY(int mapx, int mapy) {
		return (screenHeight - ((areaMap.getHeight() * 16) + (mapy + mapx) * (tileHeight / 2)));
	}

	/**
	 *  The primary draw method.
	 */
	private void drawBattleMap() {

		// sort everything (again)
		// TODO: sorting optimization
		Collections.sort((List<Drawable>) painter, Collections.reverseOrder());

		drawOrder = 0;
		Gfx.getSmallFont().setColor(0f, 0f, 0f, 1f);

		for (Drawable d : painter) {
			batch.draw(d.getSprite(), d.isoX, d.isoY);
		}

		// Draw AreaMap coordinates over tiles
		if (debug) {
			Gfx.getSmallFont().setColor(0f, 0f, 0f, 1f);
			for (int i = 0; i < areaMap.getWidth(); i++) {
				for (int j = 0; j < areaMap.getHeight(); j++) {
					Gfx.getSmallFont().draw(batch, i + "," + j, toIsoX(j, i) + 20,
							(toIsoY(j, i) + (areaMap.getTile(i, j) * 16 + 20)));
				}
			}
		}
		
		// Debug: show the painters algorithm's draw order
		if (drawOrderDebug) {
			for (Drawable d : painter) {
				drawOrder++;
				Gfx.getSmallFont().draw(batch, "" + drawOrder, d.isoX + 24, d.isoY + 20);
			}
		}
		
		// Show HP etc. info on an entity
		if (showEntityInfo) {
			for (Actor e : actors) {
				Gfx.getSmallFont().setColor(0f, 0f, 0f, 1f);
				Gfx.getSmallFont().draw(batch, e.cr.getName() + " " + e.cr.getMAString(), e.isoX-4, e.isoY+87);
				Gfx.getSystemFont().setColor(1f, 0f, 0f, 1f);
				Gfx.getSystemFont().draw(batch, e.cr.getStringHP(true), e.isoX + 4 - 2*(e.cr.getHP()), e.isoY+72);
								
			}
		}
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		guiBatch.dispose();

		Gfx.dispose();
	}

} // end class SolGDX
