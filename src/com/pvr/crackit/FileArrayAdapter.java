package com.pvr.crackit;

import java.util.List;

import com.pvr.model.FileDir;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FileArrayAdapter extends ArrayAdapter<FileDir> {

	Context context = null;
	
	// Int hold layout id
	int layoutId = 0;
	
	// hold directories and files list
	List<FileDir> fileDir = null;

	//  create files and directories list
	public FileArrayAdapter(Context _context, int _textViewResourceId,
			List<FileDir> _fileDir) {
		super(_context, _textViewResourceId, _fileDir);

		context = _context;
		layoutId = _textViewResourceId;
		fileDir = _fileDir;

	}

	// get FileDir from index
	public FileDir getFileDir(int i) {
		return fileDir.get(i);
	}

	// get view for browse file list
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(layoutId, null);
		}
		
		// set file or folder detail
		final FileDir f = fileDir.get(position);
		if (f != null) {
			TextView t1 = (TextView) v.findViewById(R.id.tv_filebrowser_1);
			TextView t2 = (TextView) v.findViewById(R.id.tv_filebrowser_2);

			// if list is file change color
			if (t1 != null)
			{
				if(!f.getData().equalsIgnoreCase("Folder"))
				{
					t1.setTextColor(Color.parseColor("#0099cc"));
				}
				t1.setText(f.getName());
			}
			if (t2 != null)
			{
				t2.setText(f.getData());
			}

		}
		return v;
	}

}
