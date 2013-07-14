package com.pvr.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pvr.model.FileDir;

import android.os.Environment;
import android.util.Log;

public class BrowseHelper {

	private File currentDir = null;
	
	// to get current files and directories information
	public BrowseHelper()
	{
		
	}
	
	// get current directory
	public File getCurrentDir()
	{
		currentDir = Environment.getExternalStorageDirectory();
		return currentDir;
	}
	
	// return list containing files and folders in current directory
	public List<FileDir> fill(File f)
	{
		File[] dirs = f.listFiles();
		
		List<FileDir> dir = new ArrayList<FileDir>();
		List<FileDir> fls = new ArrayList<FileDir>();
		
		// distribute file and directory in different list
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
		
		// sort both list and merge
		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);
		
		// set back link if it is not parent directory
		if (!f.getName().equalsIgnoreCase("sdcard"))
			dir.add(0, new FileDir(".. <go to previous directory>",
					"Parent Directory", f.getParent()));
		
		
		return dir;
		
	}
}
