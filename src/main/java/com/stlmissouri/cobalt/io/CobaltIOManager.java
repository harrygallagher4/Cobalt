package com.stlmissouri.cobalt.io;

import com.stlmissouri.cobalt.Cobalt;
import com.stlmissouri.cobalt.info.InfoSet;
import com.stlmissouri.cobalt.util.CobaltManager;

import java.io.*;

/**
 * Bibl4Pres m8.
 * @author Bibl
 *
 */
public class CobaltIOManager implements CobaltManager {

	public static final int FILE_SYSTEM_CREATE_EXCEPTION = -4;
	public static final int FILE_SYSTEM_CLOSE_EXCEPTION = -5;
	public static final int FILE_WRITE_EXCEPTION = -6;
	public static final int FILE_READ_EXCEPTION = -7;
		
	public final File COBALT_DIR;
	public final File COBALT_PLUGIN_DIR;
	public final File COBALT_KEYBIND_FILE;
		
	private Cobalt cobalt;
	
	public CobaltIOManager(Cobalt cobalt, File mcDir){
		this.cobalt = cobalt;
		
		COBALT_DIR = new File(mcDir, "cobalt");
		if(!COBALT_DIR.exists())
			COBALT_DIR.mkdir();
		
		COBALT_PLUGIN_DIR = new File(COBALT_DIR, "plugins");
		if(!COBALT_PLUGIN_DIR.exists())
			COBALT_PLUGIN_DIR.mkdir();

		COBALT_KEYBIND_FILE = new File(COBALT_DIR, "keybinds.json");
		
		try {
			if(!COBALT_KEYBIND_FILE.exists()) {
                COBALT_KEYBIND_FILE.createNewFile();
                System.out.println("Keybind file created");
            } else {
                System.out.println("Keybind file found: " + COBALT_KEYBIND_FILE.getAbsolutePath());
            }
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(FILE_SYSTEM_CREATE_EXCEPTION);
		}
	}
	
	public BufferedWriter genBindFileWriter(){
		return genWriter(COBALT_KEYBIND_FILE);
	}
	
	public BufferedReader genBindFileReader(){
		return genReader(COBALT_KEYBIND_FILE);
	}
	
	public static BufferedWriter genWriter(File file){
		try{
			return new BufferedWriter(new FileWriter(file));
		}catch(Exception e){
			e.printStackTrace();
			System.exit(FILE_WRITE_EXCEPTION);
			return null;
		}
	}
	
	public static BufferedReader genReader(File file){
		try{
			return new BufferedReader(new FileReader(file));
		}catch(Exception e){
			e.printStackTrace();
			System.exit(FILE_READ_EXCEPTION);
			return null;
		}
	}

    @Override
    public void updateInfo(InfoSet infoSet) { }
}