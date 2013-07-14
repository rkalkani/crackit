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

import com.pvr.model.Exam;

public class DatabaseHelper extends SQLiteOpenHelper {

	// All Static variables
	
	// Database Version
	private static final int DATABSE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "crackit.db";

	// Table Name
	private static final String TABLE_EXAMLIST = "examlist";

	// ExamList Column Name
	private static final String KEY_ID = "id";
	private static final String EXAM_NAME = "examname";
	private static final String EXAM_TYPE = "examtype";
	private static final String E_DATE = "date";
	private static final String E_TIME = "time";
	private static final String E_DURATIONOFEXAM = "durationofexam";
	private static final String E_NOOFQUESTION = "noofquestion";
	private static final String E_QUESTIONSET = "questionset";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABSE_VERSION);
	}

	// Creating Table
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE_EXAMLIST = "CREATE TABLE " + TABLE_EXAMLIST + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + EXAM_NAME + " TEXT,"
				+ EXAM_TYPE + " INTEGER," + E_DATE + " TEXT," + E_TIME
				+ " TEXT," + E_DURATIONOFEXAM + " INTEGER," + E_NOOFQUESTION
				+ " INTEGER," + E_QUESTIONSET + " TEXT" + ")";
		try {
			db.execSQL(CREATE_TABLE_EXAMLIST);
		} catch (SQLException e) {
			Log.d("tag", "SQLException in onCreate");
		}

	}

	// Upgrading Table
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// Delete Table
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMLIST);

		// Create Table
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new exam
	public Long addExam(Exam exam) throws SQLException {
		SQLiteDatabase db = null;
		db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(EXAM_NAME, exam.getExamname()); // Exam Name
		values.put(EXAM_TYPE, exam.getExamtype()); // Exam Type
		values.put(E_DATE, exam.getDate()); // Exam Type
		values.put(E_TIME, exam.getTime()); // Exam Type
		values.put(E_DURATIONOFEXAM, exam.getDurationofexam()); // Exam Type
		values.put(E_NOOFQUESTION, exam.getNoofquestion()); // Exam Type
		values.put(E_QUESTIONSET, exam.getQuestionset()); // Exam Type

		// Inserting Row
		long returnValue = db.insert(TABLE_EXAMLIST, null, values);
		db.close(); // Closing database connection
		return returnValue;
	}

	// Getting all exams
	public List<Exam> getAllexams() throws SQLException {
		List<Exam> examList = new ArrayList<Exam>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_EXAMLIST;

		SQLiteDatabase db = null;
		db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Exam exam = new Exam();
				exam.setId(Integer.parseInt(cursor.getString(0)));
				exam.setExamname(cursor.getString(1));
				exam.setExamtype(Integer.parseInt(cursor.getString(2)));
				exam.setDate(cursor.getString(3));
				exam.setTime(cursor.getString(4));
				exam.setDurationofexam(Integer.parseInt(cursor.getString(5)));
				exam.setNoofquestion(Integer.parseInt(cursor.getString(6)));
				exam.setQuestionset(cursor.getString(7));
				// Adding exam to list
				examList.add(exam);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		// return exam list
		return examList;
	}
	
	// to get all exam in reverse order so it display latest created exam first
	public List<Exam> getAllexamsReverse() throws SQLException {
		List<Exam> examList = new ArrayList<Exam>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_EXAMLIST;

		SQLiteDatabase db = null;
		db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToLast()) {
			do {
				Exam exam = new Exam();
				exam.setId(Integer.parseInt(cursor.getString(0)));
				exam.setExamname(cursor.getString(1));
				exam.setExamtype(Integer.parseInt(cursor.getString(2)));
				exam.setDate(cursor.getString(3));
				exam.setTime(cursor.getString(4));
				exam.setDurationofexam(Integer.parseInt(cursor.getString(5)));
				exam.setNoofquestion(Integer.parseInt(cursor.getString(6)));
				exam.setQuestionset(cursor.getString(7));
				// Adding exam to list
				examList.add(exam);
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();

		// return exam list
		return examList;
	}

	// Getting exams By name
	public List<Exam> getExamsByName(String nameToSearch) throws SQLException {
		List<Exam> examList = new ArrayList<Exam>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_EXAMLIST + " WHERE "
				+ EXAM_NAME + " LIKE '%" + nameToSearch + "%'" ;
		

		SQLiteDatabase db = null;
		db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Exam exam = new Exam();
				exam.setId(Integer.parseInt(cursor.getString(0)));
				exam.setExamname(cursor.getString(1));
				exam.setExamtype(Integer.parseInt(cursor.getString(2)));
				exam.setDate(cursor.getString(3));
				exam.setTime(cursor.getString(4));
				exam.setDurationofexam(Integer.parseInt(cursor.getString(5)));
				exam.setNoofquestion(Integer.parseInt(cursor.getString(6)));
				exam.setQuestionset(cursor.getString(7));
				// Adding exam to list
				examList.add(exam);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		// return exam list
		return examList;
	}

	// get exam from exam id
	public Exam getExam(int id) throws SQLException {
		Exam exam = new Exam();
		String selectQuery = "SELECT  * FROM " + TABLE_EXAMLIST + " WHERE "
				+ KEY_ID + "=" + id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// get exam and move through cursor and return single exam
		if (cursor.moveToFirst()) {
			do {
				exam.setId(Integer.parseInt(cursor.getString(0)));
				exam.setExamname(cursor.getString(1));
				exam.setExamtype(Integer.parseInt(cursor.getString(2)));
				exam.setDate(cursor.getString(3));
				exam.setTime(cursor.getString(4));
				exam.setDurationofexam(Integer.parseInt(cursor.getString(5)));
				exam.setNoofquestion(Integer.parseInt(cursor.getString(6)));
				exam.setQuestionset(cursor.getString(7));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		// Return Exam
		return exam;
	}

	// Deleting single exam
	public void deleteExam(Exam exam) throws SQLException {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_EXAMLIST, KEY_ID + " = ?",
				new String[] { String.valueOf(exam.getId()) });
		db.close();
	}

	// Getting exams Count
	public int getExamsCount() throws SQLException {
		String countQuery = "SELECT  * FROM " + TABLE_EXAMLIST;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		// return count

		cursor.close();
		db.close();
		return count;
	}

	// Edit the row value
	public long update(Exam exam) throws SQLException {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ID, exam.getId());
		values.put(EXAM_NAME, exam.getExamname()); // Exam Name
		values.put(EXAM_TYPE, exam.getExamtype()); // Exam Type
		values.put(E_DATE, exam.getDate()); // Exam Type
		values.put(E_TIME, exam.getTime()); // Exam Type
		values.put(E_DURATIONOFEXAM, exam.getDurationofexam()); // Exam Type
		values.put(E_NOOFQUESTION, exam.getNoofquestion()); // Exam Type
		values.put(E_QUESTIONSET, exam.getQuestionset()); // Exam Type
		long returnValue = db.update(TABLE_EXAMLIST, values, KEY_ID + " = ? ",
				new String[] { String.valueOf(exam.getId()) });
		// String[] args: The arguments of the WHERE clause
		db.close();
		return returnValue;
	}

}
