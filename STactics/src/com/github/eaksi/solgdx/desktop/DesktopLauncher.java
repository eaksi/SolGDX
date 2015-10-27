package com.github.eaksi.solgdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.eaksi.solgdx.SolGDX;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
        config.title = "SolTactics";
        config.width = 640;
        config.height = 400;
        
        new LwjglApplication(new SolGDX(), config);
	}
}
