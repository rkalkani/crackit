package com.pvr.model;

import java.io.Serializable;

import dateandtime.DateAndTime;

public class Result implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 101332;
	private int id;
	private int examid;
	private String examname;
	private int examtype;
	private int durationofexam;
	private int noofquestion;
	private String questionset;

	private String date;
	private String time;

	private int rightans;
	private int wrongans;
	private int skipans;

	private String percentage;

	// Empty Constructor
	public Result() {
		// Default Constructor
	}

	// Constructor
	public Result(int _id, int _examid, String _examname, int _examtype,
			int _durationofexam, int _noofquestion, String _questionset,
			String _date, String _time, int _rightans, int _wrongans,
			int _skipans, String _percentage) {
		this.id = _id;
		this.examid = _examid;
		this.examname = _examname;
		this.examtype = _examtype;
		this.durationofexam = _durationofexam;
		this.noofquestion = _noofquestion;
		this.questionset = _questionset;
		this.date = _date;
		this.time = _time;
		this.rightans = _rightans;
		this.wrongans = _wrongans;
		this.skipans = _skipans;
		this.percentage = _percentage;
	}

	// Constructor
	public Result(int _examid, String _examname, int _examtype,
			int _durationofexam, int _noofquestion, String _questionset,
			String _date, String _time, int _rightans, int _wrongans,
			int _skipans, String _percentage) {
		this.examid = _examid;
		this.examname = _examname;
		this.examtype = _examtype;
		this.durationofexam = _durationofexam;
		this.noofquestion = _noofquestion;
		this.questionset = _questionset;
		this.date = _date;
		this.time = _time;
		this.rightans = _rightans;
		this.wrongans = _wrongans;
		this.skipans = _skipans;
		this.percentage = _percentage;
	}

	public Result(int _examid, String _examname, int _examtype,
			int _durationofexam, int _noofquestion, String _questionset,
			int _rightans, int _wrongans, int _skipans, String _percentage) {
		this.examid = _examid;
		this.examname = _examname;
		this.examtype = _examtype;
		this.durationofexam = _durationofexam;
		this.noofquestion = _noofquestion;
		this.questionset = _questionset;
		this.setDate();
		this.setTime();
		this.rightans = _rightans;
		this.wrongans = _wrongans;
		this.skipans = _skipans;
		this.percentage = _percentage;
	}

	public int getId() {
		return id;
	}

	public void setId(int _id) {
		this.id = _id;
	}

	public int getExamid() {
		return examid;
	}

	public void setExamid(int _examid) {
		this.examid = _examid;
	}

	public String getExamname() {
		return examname;
	}

	public void setExamname(String _examname) {
		this.examname = _examname;
	}

	public int getExamtype() {
		return examtype;
	}

	public void setExamtype(int _examtype) {
		this.examtype = _examtype;
	}

	public int getDurationofexam() {
		return durationofexam;
	}

	public void setDurationofexam(int _durationofexam) {
		this.durationofexam = _durationofexam;
	}

	public int getNoofquestion() {
		return noofquestion;
	}

	public void setNoofquestion(int _noofquestion) {
		this.noofquestion = _noofquestion;
	}

	public String getQuestionset() {
		return questionset;
	}

	public void setQuestionset(String _questionset) {
		this.questionset = _questionset;
	}

	public String getDate() {
		return date;
	}

	public void setDate() {
		String _date;
		DateAndTime mDateAndTime = new DateAndTime();
		_date = mDateAndTime.getDate();
		this.date = _date;
	}

	public void setDate(String _date) {
		this.date = _date;
	}

	public String getTime() {
		return time;
	}

	public void setTime() {
		String _time;
		DateAndTime mDateAndTime = new DateAndTime();
		_time = mDateAndTime.getTime();
		this.time = _time;
	}

	public void setTime(String _time) {
		this.time = _time;
	}

	public int getRightans() {
		return rightans;
	}

	public void setRightans(int _rightans) {
		this.rightans = _rightans;
	}

	public int getWrongans() {
		return wrongans;
	}

	public void setWrongans(int _wrongans) {
		this.wrongans = _wrongans;
	}

	public int getSkipans() {
		return skipans;
	}

	public void setSkipans(int _skipans) {
		this.skipans = _skipans;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String _percentage) {
		this.percentage = _percentage;
	}

}
