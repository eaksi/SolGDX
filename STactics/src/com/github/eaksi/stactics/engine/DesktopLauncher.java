package com.github.eaksi.stactics.engine;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
        config.title = "SolTactics";
        config.width = 640;
        config.height = 480;
        
        new LwjglApplication(new SolGDX(), config);
	}
}
