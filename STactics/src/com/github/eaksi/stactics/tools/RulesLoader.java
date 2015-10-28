package com.github.eaksi.stactics.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class RulesLoader {
	
	private static Properties defaultProps = new Properties();
	private static Properties applicationProps;
	
	public static void tempSave() {
		try {
			FileOutputStream out = new FileOutputStream("appProperties");
			applicationProps.store(out, "---No Comment---");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void tempLoad() {
		
		// create and load default properties
		FileInputStream in;
		try {
			in = new FileInputStream("defaultProperties");
			defaultProps.load(in);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		// now load properties 
		// from last invocation
		try {
			in = new FileInputStream("appProperties");
			applicationProps = new Properties(defaultProps);
			applicationProps.load(in);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	}
	
}
