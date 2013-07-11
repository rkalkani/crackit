package com.pvr.model;

public class FileDir implements Comparable<FileDir>{

	private String name;
	private String data;
	private String path;

	public FileDir(String _name, String _data, String _path) {
		name = _name;
		data = _data;
		path = _path;
	}

	public String getName() {
		return name;
	}

	public String getData() {
		return data;
	}

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
