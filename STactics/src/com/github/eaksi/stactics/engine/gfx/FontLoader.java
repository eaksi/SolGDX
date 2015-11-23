package com.github.eaksi.stactics.engine.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontLoader {

	public static BitmapFont getFont() {

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/Klill-Light.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 20;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();		// generator no longer needed
	
		//font = new BitmapFont();
	    font.setColor(Color.BLACK);		// set default color
	    
	    return font;
	}
}
