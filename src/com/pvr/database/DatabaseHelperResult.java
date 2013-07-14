package com.pvr.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pvr.model.Result;

public class DatabaseHelperResult extends SQLiteOpenHelper {

	// All Static variables
	// Database Version

	private static final int DATABSE_VERSION = 1;

	// Database Name

	private static final String DATABASE_NAME = "crackit_result.db";

	// Table Name

	private static final String TABLE_RESULTLIST = "resultlist";

	// ResultList Column Name

	private static final String KEY_ID = "id";
	private static final String EXAM_ID = "examid";
	private static final String EXAM_NAME = "examname";
	private static final String EXAM_TYPE = "examtype";
	private static final String E_DURATIONOFEXAM = "durationofexam";
	private static final String E_NOOFQUESTION = "noofquestion";
	private static final String E_QUESTIONSET = "questionset";

	private static final String R_DATE = "date";
	private static final String R_TIME = "time";

	private static final String RIGHTANSWER = "rightans";
	private static final String WRONGANSWER = "wrongans";
	private static final String SKIPANSWER = "skipans";
	private static final String PERCENTAGE = "percentage";

	public DatabaseHelperResult(Context context) {
		super(context, DATABASE_NAME, null, DATABSE_VERSION);
	}

	// create result table on creation of database
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE_RESULTLIST = "CREATE TABLE " + TABLE_RESULTLIST
				+ "(" + KEY_ID + " INTEGER PRIMARY KEY," + EXAM_ID
				+ " INTEGER," + EXAM_NAME + " TEXT," + EXAM_TYPE + " INTEGER,"
				+ E_DURATIONOFEXAM + " INTEGER," + E_NOOFQUESTION + " INTEGER,"
				+ E_QUESTIONSET + " TEXT," + R_DATE + " TEXT," + R_TIME
				+ " TEXT," + RIGHTANSWER + " INTEGER," + WRONGANSWER
				+ " INTEGER," + SKIPANSWER + " INTEGER," + PERCENTAGE + " TEXT"
				+ ")";
		try {
			Log.d("table create", "before execSQL onCreate");
			db.execSQL(CREATE_TABLE_RESULTLIST);
			Log.d("table create", "after execSQL onCreate");
		} catch (SQLException e) {
			Log.d("tag", "SQLException in onCreate");
		}

	}

	// upgrading table
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// delete table
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTLIST);

		// create table
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// add new result object to database
	public Long addResult(Result result) throws SQLException {
		SQLiteDatabase db = null;
		db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(EXAM_ID, result.getExamid());
		values.put(EXAM_NAME, result.getExamname());
		values.put(EXAM_TYPE, result.getExamtype());
		values.put(E_DURATIONOFEXAM, result.getDurationofexam());
		values.put(E_NOOFQUESTION, result.getNoofquestion());
		values.put(E_QUESTIONSET, result.getQuestionset());
		values.put(R_DATE, result.getDate());
		values.put(R_TIME, result.getTime());
		values.put(RIGHTANSWER, result.getRightans());
		values.put(WRONGANSWER, result.getWrongans());
		values.put(SKIPANSWER, result.getSkipans());
		values.put(PERCENTAGE, result.getPercentage());

		// add result
		long returnValue = db.insert(TABLE_RESULTLIST, null, values);
		db.close(); // closing database connection
		return returnValue;
	}

	// Getting all results
	public List<Result> getAllResult() throws SQLException {
		List<Result> resultList = new ArrayList<Result>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_RESULTLIST;

		SQLiteDatabase db = null;
		db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Result result = new Result();
				result.setId(Integer.parseInt(cursor.getString(0)));
				result.setExamid(Integer.parseInt(cursor.getString(1)));
				result.setExamname(cursor.getString(2));
				result.setExamtype(Integer.parseInt(cursor.getString(3)));
				result.setDurationofexam(Integer.parseInt(cursor.getString(4)));
				result.setNoofquestion(Integer.parseInt(cursor.getString(5)));
				result.setQuestionset(cursor.getString(6));
				result.setDate(cursor.getString(7));
				result.setTime(cursor.getString(8));
				result.setRightans(Integer.parseInt(cursor.getString(9)));
				result.setWrongans(Integer.parseInt(cursor.getString(10)));
				result.setSkipans(Integer.parseInt(cursor.getString(11)));
				result.setPercentage(cursor.getString(12));
				// Adding result to list
				resultList.add(result);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close(); // close database connection

		//return result list
		return resultList;
	}
	
	// to get all result in reverse order so it display latest created result first
	public List<Result> getAllResultReverse() throws SQLException {
		List<Result> resultList = new ArrayList<Result>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_RESULTLIST;

		SQLiteDatabase db = null;
		db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToLast()) {
			do {
				Result result = new Result();
				result.setId(Integer.parseInt(cursor.getString(0)));
				result.setExamid(Integer.parseInt(cursor.getString(1)));
				result.setExamname(cursor.getString(2));
				result.setExamtype(Integer.parseInt(cursor.getString(3)));
				result.setDurationofexam(Integer.parseInt(cursor.getString(4)));
				result.setNoofquestion(Integer.parseInt(cursor.getString(5)));
				result.setQuestionset(cursor.getString(6));
				result.setDate(cursor.getString(7));
				result.setTime(cursor.getString(8));
				result.setRightans(Integer.parseInt(cursor.getString(9)));
				result.setWrongans(Integer.parseInt(cursor.getString(10)));
				result.setSkipans(Integer.parseInt(cursor.getString(11)));
				result.setPercentage(cursor.getString(12));
				// Adding result to list
				resultList.add(result);
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
		// return result list
		return resultList;
	}
	
	
	// Getting results By name
	public List<Result> getResultByName(String nameToSearch)
			throws SQLException {
		
		List<Result> resultList = new ArrayList<Result>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_RESULTLIST + " WHERE "
				+ EXAM_NAME + " LIKE '%" + nameToSearch + "%'" ;
		

		SQLiteDatabase db = null;
		db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Result result = new Result();
				result.setId(Integer.parseInt(cursor.getString(0)));
				result.setExamid(Integer.parseInt(cursor.getString(1)));
				result.setExamname(cursor.getString(2));
				result.setExamtype(Integer.parseInt(cursor.getString(3)));
				result.setDurationofexam(Integer.parseInt(cursor.getString(4)));
				result.setNoofquestion(Integer.parseInt(cursor.getString(5)));
				result.setQuestionset(cursor.getString(6));
				result.setDate(cursor.getString(7));
				result.setTime(cursor.getString(8));
				result.setRightans(Integer.parseInt(cursor.getString(9)));
				result.setWrongans(Integer.parseInt(cursor.getString(10)));
				result.setSkipans(Integer.parseInt(cursor.getString(11)));
				result.setPercentage(cursor.getString(12));

				// Adding result to list
				resultList.add(result);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		// return result list
		return resultList;
	}

	// get result from result id
	public Result getResult(int id) throws SQLException {
		Result result = new Result();
		String selectQuery = "SELECT  * FROM " + TABLE_RESULTLIST + " WHERE "
				+ KEY_ID + "=" + id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// get result and move through cursor and return single result
		if (cursor.moveToFirst()) {
			do {
				result.setId(Integer.parseInt(cursor.getString(0)));
				result.setExamid(Integer.parseInt(cursor.getString(1)));
				result.setExamname(cursor.getString(2));
				result.setExamtype(Integer.parseInt(cursor.getString(3)));
				result.setDurationofexam(Integer.parseInt(cursor.getString(4)));
				result.setNoofquestion(Integer.parseInt(cursor.getString(5)));
				result.setQuestionset(cursor.getString(6));
				result.setDate(cursor.getString(7));
				result.setTime(cursor.getString(8));
				result.setRightans(Integer.parseInt(cursor.getString(9)));
				result.setWrongans(Integer.parseInt(cursor.getString(10)));
				result.setSkipans(Integer.parseInt(cursor.getString(11)));
				result.setPercentage(cursor.getString(12));

			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		
		// Return result
		return result;
	}

	// Deleting single result
	public void deleteResult(Result result) throws SQLException {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_RESULTLIST, KEY_ID + " = ?",
				new String[] { String.valueOf(result.getId()) });
		db.close();
	}

	// Getting results Count
	public int getResultsCount() throws SQLException {
		String countQuery = "SELECT  * FROM " + TABLE_RESULTLIST;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();

		cursor.close();
		db.close();
		// return count
		return count;
	}

	// Edit the row value
	public long update(Result result) throws SQLException {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ID, result.getId());
		values.put(EXAM_ID, result.getExamid());
		values.put(EXAM_NAME, result.getExamname());
		values.put(EXAM_TYPE, result.getExamtype());
		values.put(E_DURATIONOFEXAM, result.getDurationofexam());
		values.put(E_NOOFQUESTION, result.getNoofquestion());
		values.put(E_QUESTIONSET, result.getQuestionset());
		values.put(R_DATE, result.getDate());
		values.put(R_TIME, result.getTime());
		values.put(RIGHTANSWER, result.getRightans());
		values.put(WRONGANSWER, result.getWrongans());
		values.put(SKIPANSWER, result.getSkipans());
		values.put(PERCENTAGE, result.getPercentage());

		long returnValue = db.update(TABLE_RESULTLIST, values,
				KEY_ID + " = ? ",
				new String[] { String.valueOf(result.getId()) });
		
		// String[] args: The arguments of the WHERE clause
		db.close();
		return returnValue;
	}

}
