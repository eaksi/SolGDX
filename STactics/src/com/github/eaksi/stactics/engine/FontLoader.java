package com.github.eaksi.stactics.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * This class does the TrueType font loading.
 *
 */
public class FontLoader {

	//
	
	public enum Type {
		MAIN, SMALL
	}

	public static BitmapFont getFont(Type type) {

		String fontPath = "";
		int size = 10;
		FreeTypeFontGenerator generator;
		FreeTypeFontParameter parameter;

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

		generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
		parameter = new FreeTypeFontParameter();
		parameter.size = size;

		BitmapFont font = generator.generateFont(parameter);
		generator.dispose(); // generator no longer needed

		font.setColor(Color.BLACK); // set default color

		return font;
	}
	
	public static BitmapFont getSystemFont() {
		return new BitmapFont();
	}
	
}
