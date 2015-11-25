package com.github.eaksi.stactics.engine.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontLoader {

	public enum Type {
		MAIN,
		SMALL
	}
	
	public static BitmapFont getFont(Type type) {

		String fontPath = "";
		int size = 10;
		
		switch (type) {
		case MAIN:
			fontPath = "data/Klill-Light.ttf";
			size = 20;
			break;
		case SMALL:
			fontPath = "data/Klill-Light.ttf";
			size = 14;
			break;
		default:
			System.err.println("Error: Invalid font type");
			break;
		}
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();		// generator no longer needed
	
		//font = new BitmapFont();
	    font.setColor(Color.BLACK);		// set default color
	    
	    return font;
	}


}
