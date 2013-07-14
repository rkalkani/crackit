package com.pvr.common;

import java.util.ArrayList;

import android.os.Bundle;

public class PaperCreateConstant {
	
	// class hold information for question paper generated from question set

	public ArrayList<String> ArrayList_Question_ID = null;
	public ArrayList<String> ArrayList_Questions = null;
	public ArrayList<String> ArrayList_Option_1 = null;
	public ArrayList<String> ArrayList_Option_2 = null;
	public ArrayList<String> ArrayList_Option_3 = null;
	public ArrayList<String> ArrayList_Option_4 = null;
	public ArrayList<String> ArrayList_Correct_Answer = null;
	public Bundle mBundle = null;

	public PaperCreateConstant() {
		ArrayList_Question_ID = new ArrayList<String>();
		ArrayList_Questions = new ArrayList<String>();
		ArrayList_Option_1 = new ArrayList<String>();
		ArrayList_Option_2 = new ArrayList<String>();
		ArrayList_Option_3 = new ArrayList<String>();
		ArrayList_Option_4 = new ArrayList<String>();
		ArrayList_Correct_Answer = new ArrayList<String>();
		mBundle = new Bundle();
	}
}
