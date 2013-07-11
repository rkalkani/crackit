package com.pvr.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class SelectedItem {

	private List<Integer> itemList = new ArrayList<Integer>();
	private int count = 0;

	private void incrementCount() {
		count++;
	}

	private void decrementCount() {
		count--;
	}

	public int getCount() {
		return count;
	}

	public int change(int id) {
		int flag = 0;
		Log.d("lol", "lolm lol");
		flag = itemList.indexOf(id);
		if (flag == -1) {
			addItem(id);
		} else {
			removeItem(id);
		}
		return 1;
	}

	private void removeItem(int id) {
		itemList.add(id);
		incrementCount();
	}

	private void addItem(int id) {
		itemList.remove(id);
		decrementCount();
	}

}
