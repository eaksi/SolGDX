package com.github.eaksi.stactics.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.eaksi.stactics.db.World;
import com.github.eaksi.stactics.engine.SolGDX;

public class DesktopLauncher {
	
	public static void main (String[] arg) {
	
		World.initialize();
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "SolTactics";
		config.width = 1024;
		config.height = 800;
        
        new LwjglApplication(new SolGDX(), config);
	}
}
