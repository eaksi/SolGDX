package com.github.eaksi.stactics.engine;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.eaksi.stactics.db.BattleMap;
import com.github.eaksi.stactics.db.Creature;
import com.github.eaksi.stactics.engine.gfx.Drawable;
import com.github.eaksi.stactics.engine.gfx.Entity;
import com.github.eaksi.stactics.engine.gfx.Entity.Direction;
import com.github.eaksi.stactics.engine.gfx.Tile;

/**
 * SolGDX is the coordinating class for the graphics engine.
 *
 */
public class SolGDX extends ApplicationAdapter {

	// TODO: ini file reader
	// FIXME: wall graphics
	
	// debug flags and related things
	boolean debug = false;
	boolean drawOrderDebug = false;
	private int drawOrder = 0;
	boolean autoRefreshMovesDebug = true;
	boolean hitBattlersDebug = true;
	
	boolean showEntityInfo = false;
	
	Camera camera;
	private SpriteBatch batch; // primary SpriteBatch for graphics
	private SpriteBatch guiBatch; // GUI SpriteBatch, does not move with camera

	final int screenWidth = 1024; // screen resolution
	final int screenHeight = 800;

	// XXX: TEMP if the engine is animating movement -> move keys disabled
	boolean chAnimating = false; 

	private int tileWidth = 64;
	private int tileHeight = 32;

	private BattleMap battleMap;

	Vector<Entity> entities;
	int nr = 0; // current entity Number

	Vector<Drawable> painter;

	// debug
	public int mouseScreenX = -999;
	public int mouseScreenY = -999;
	public int mouseIsoX = -999;
	public int mouseIsoY = -999;
	public int mouseDragX = -999;
	public int mouseDragY = -999;

	@Override
	public void create() {

		battleMap = new BattleMap();
		entities = new Vector<Entity>();
		entities.add(new Entity(new Creature(), 0, 4));
		entities.add(new Entity(new Creature(), 4, 5));
		entities.add(new Entity(new Creature(), 6, 4));

		System.out.println("***ENTITIES***");
		for (Entity e : entities) {
			System.out.println(e.cr.getId() + ": " + e.cr.getFullName());
		}
		System.out.println("**************");


		// setup camera
		camera = new Camera(screenWidth, screenHeight);

		batch = new SpriteBatch();
		guiBatch = new SpriteBatch();

		Gfx.initialize(); // initialize Gfx class, load graphics etc.

		setEntityLocations(); // initial setup of entity locations XXX: move

		initializePainter(); // initialize painter's algorithm (make & sort Drawables)

		/* print the BattleMap width and height and w/h difference
		 *  (negative = more width than height)	 */
		if (debug) {
			System.out.println("battleMap.getWidth() = " + battleMap.getWidth() + "  battleMap.getHeight() = "
					+ battleMap.getHeight() + "  difference = " + (battleMap.getWidth() - battleMap.getHeight()));
		}

	}

	@Override
	public void render() {


		Gdx.gl.glClearColor(1, 1, 1, 1); 					// white screen
		//Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1); 		// light grey screen
		//Gdx.gl.glClearColor(0, 0, 0, 1);			 		// black screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		Controls.getInput(this); // keyboard controls handled at this point
		// update animation frames for entities
		for (Entity e : entities) {
			e.updateAnimFrame();
		}
		
		chAnimating = entities.get(nr).getAnimation() != Entity.Animation.IDLE; // if no animation, can move
		
		batch.setProjectionMatrix(camera.combined);

		camera.updateZoom();

		// draw everything
		batch.begin();
		drawIsometric();
		batch.end();

		guiBatch.begin();
		GUI.draw(this, guiBatch); // draw GUI and possible debug data
		guiBatch.end();

		// update FPS counter on window title
		Gdx.graphics
				.setTitle("SolGDX     FPS: " + Gdx.graphics.getFramesPerSecond() + " Zoom: " + camera.getZoomLevel());

	}

	private void setEntityLocations() {
		// set starting locations of entities
		for (Entity e : entities) {
			e.isoX = toIsoX(e.tileY, e.tileX) + 16;
			e.isoY = toIsoY(e.tileY, e.tileX) + 22; // XXX: sprite/tile sizes hack
			
		}
	}

	private void initializePainter() {

		painter = new Vector<Drawable>();
		Tile tile;

		// add tiles
		for (int i = 0; i < battleMap.getWidth(); i++) {
			for (int j = 0; j < battleMap.getHeight(); j++) {
				if (battleMap.getTile(i, j) == 0) {
					tile = new Tile(i, j, toIsoX(j, i), toIsoY(j, i), toIsoY(j, i), true);
				} else {
					tile = new Tile(i, j, toIsoX(j, i), toIsoY(j, i) + battleMap.getTile(i, j) * 16, toIsoY(j, i),
							false);
				}

				painter.add((Drawable) tile);

				// debug
				if (debug)
					System.out.println("draw tile: (" + tile.tileX + "," + tile.tileY + ") z-depth: " + tile.getZ()
							+ " height: " + battleMap.getTile(i, j) + " water: " + tile.isWater());
			}
		}

		// add characters
		for (Entity e : entities) {
			// XXX: z position guessed (but tested, need frame by frame
			// analysis, height changes testing and gfx)
			e.setZ(e.isoY - 27);
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

		entities.get(nr).setHeading(d);

		switch (entities.get(nr).getHeading()) {
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
		if (battleMap.getTile((entities.get(nr).tileX + tryX), entities.get(nr).tileY + tryY) != 1) {
			printMoveDebug("Can't move (terrain): " + entities.get(nr).getHeading(), tryX, tryY);
			return;
		}

		// test if able to move because of other entities
		for (Entity e : entities) {
			if (entities.get(nr).tileX + tryX == e.tileX && entities.get(nr).tileY + tryY == e.tileY) {
				if (!hitBattlersDebug) {
					printMoveDebug("Can't move, " + e.cr.getName() + " (id:" + e.cr.getId() + ") is in the way: "
							+ entities.get(nr).getHeading(), tryX, tryY);
				} else if (entities.get(nr).cr.getActions() > 0) { // XXX: TEMP attack code
					entities.get(nr).animFrameNr = 0;
					entities.get(nr).setAnimation(Entity.Animation.ATTACK);
					chAnimating = true;

					entities.get(nr).cr.spendAction();	
					
					// animate victim
					e.cr.applyDamage(1);
					e.animFrameNr = 0;
					e.setAnimation(Entity.Animation.HURT);
				} else {
					System.out.println(entities.get(nr).cr.getName() + " ran out of actions!");
				}
				return; // NOTE!: "return;" here
			}
		}

		// refresh moves automatically if debug flag for that is on
		if (entities.get(nr).cr.getMoves() <= 0 && autoRefreshMovesDebug) entities.get(nr).cr.startNewTurn(); 
		
		
		// check if entity has moves left
		// TODO: moves where more than 1 point of move needed
		if (entities.get(nr).cr.getMoves() > 0) {
			
			printMoveDebug("" + entities.get(nr).getHeading(), tryX, tryY);
			entities.get(nr).tileX += tryX;
			entities.get(nr).tileY += tryY;
	
			// if everything okay, set movement in motion
			entities.get(nr).animFrameNr = 0;
			entities.get(nr).setAnimation(Entity.Animation.WALK);
			chAnimating = true;
			
			entities.get(nr).cr.spendMove();
			

		} else {
			System.out.println(entities.get(nr).cr.getName() + " ran out of moves!");
		}
	}

	private void printMoveDebug(String dir, int x, int y) {
		System.out.println(dir + "(" + entities.get(nr).tileX + "," + entities.get(nr).tileY + ") -> ("
				+ (entities.get(nr).tileX + x) + "," + (entities.get(nr).tileY + y) + ")");
	}

	/**
	 *  Get the isometric projection coordinate X, given tilemap X and Y as parameters
	 */
	private int toIsoX(int mapx, int mapy) {
		return (screenWidth - ((battleMap.getWidth() * 32) + (mapy - mapx) * (tileWidth / 2)));
	}

	/**
	 *  Get the isometric projection coordinate Y, given tilemap X and Y as parameters
	 */
	private int toIsoY(int mapx, int mapy) {
		return (screenHeight - ((battleMap.getHeight() * 16) + (mapy + mapx) * (tileHeight / 2)));
	}

	/**
	 *  The primary draw method.
	 */
	private void drawIsometric() {

		// sort everything (again)
		// TODO: sorting optimization
		Collections.sort((List<Drawable>) painter, Collections.reverseOrder());

		drawOrder = 0;
		Gfx.getSmallFont().setColor(0f, 0f, 0f, 1f);

		for (Drawable d : painter) {
			batch.draw(d.getSprite(), d.isoX, d.isoY);
		}

		// Draw BattleMap coordinates over tiles
		if (debug) {
			Gfx.getSmallFont().setColor(0f, 0f, 0f, 1f);
			for (int i = 0; i < battleMap.getWidth(); i++) {
				for (int j = 0; j < battleMap.getHeight(); j++) {
					Gfx.getSmallFont().draw(batch, i + "," + j, toIsoX(j, i) + 20,
							(toIsoY(j, i) + (battleMap.getTile(i, j) * 16 + 20)));
				}
			}
		}

		//FIXME: placed incorrectly
		if (drawOrderDebug) {
			for (Drawable d : painter) {
				drawOrder++;
				Gfx.getSmallFont().draw(batch, "" + drawOrder, d.isoX + 24, d.isoY + 20);
			}
		}
		
		if (showEntityInfo) {
			for (Entity e : entities) {
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
