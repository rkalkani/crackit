package com.pvr.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pvr.model.FileDir;

import android.os.Environment;
import android.util.Log;

public class BrowseHelper {

	private File currentDir;
	
	public BrowseHelper()
	{
		
	}
	
	public File getCurrentDir()
	{
		currentDir = Environment.getExternalStorageDirectory();
		return currentDir;
	}
	
	public List<FileDir> fill(File f)
	{
		File[] dirs = f.listFiles();
		
		//this.setTitle("Current Dir: " + f.getName());
		List<FileDir> dir = new ArrayList<FileDir>();
		List<FileDir> fls = new ArrayList<FileDir>();
		try {
			for (File ff : dirs) {
				if (ff.isDirectory()) {
					dir.add(new FileDir(ff.getName(), "Folder", ff
							.getAbsolutePath()));
				} else {

					try {
						String fileextension = ff.getName();
						Log.d("tag",fileextension);
						String filenameArray[] = fileextension.split("\\.");
						fileextension = filenameArray[filenameArray.length - 1];
						Log.d("tag",fileextension);
						if (fileextension.equalsIgnoreCase("xml")) {
							fls.add(new FileDir(ff.getName(), "File Size: "
									+ ff.length(), ff.getAbsolutePath()));
						}
					} catch (Exception e) {
						Log.d("tag", e.toString());
						fls.add(new FileDir(ff.getName(), "File Size: "
								+ ff.length(), ff.getAbsolutePath()));
					}

				}
			}
		} catch (Exception e) {
			Log.d("tag", e.toString());
		}
		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);
		if (!f.getName().equalsIgnoreCase("sdcard"))
			dir.add(0, new FileDir(".. <go to previous directory>",
					"Parent Directory", f.getParent()));
		
		
		return dir;
		
	}
}
