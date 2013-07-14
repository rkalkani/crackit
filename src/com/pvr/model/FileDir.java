package com.pvr.model;

import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale")
public class FileDir implements Comparable<FileDir> {

	private String name = null; // store file name
	private String data = null; // store whether it is file or folder and file
								// size
	private String path = null; // store absolute path

	public FileDir(String _name, String _data, String _path) {
		name = _name;
		data = _data;
		path = _path;
	}

	// get file name
	public String getName() {
		return name;
	}

	// get file data
	public String getData() {
		return data;
	}

	// get file absolute path
	public String getPath() {
		return path;
	}

	@Override
	public int compareTo(FileDir o) {
		if (this.name != null)
			return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
		else
			throw new IllegalArgumentException();
	}
}
