package com.pvr.model;

import java.io.Serializable;

import dateandtime.DateAndTime;

public class Result implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 101332;
	private int id = 0;
	private int examid = 0;
	private String examname = null;
	private int examtype = 0;
	private int durationofexam = 0;
	private int noofquestion = 0;
	private String questionset = null;

	private String date = null;
	private String time = null;

	private int rightans = 0;
	private int wrongans = 0;
	private int skipans = 0;

	private String percentage = null;

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

	// get id
	public int getId() {
		return id;
	}

	// set id
	public void setId(int _id) {
		this.id = _id;
	}

	// get exam id
	public int getExamid() {
		return examid;
	}

	// set exam id
	public void setExamid(int _examid) {
		this.examid = _examid;
	}

	// get exam name
	public String getExamname() {
		return examname;
	}

	// set exam name
	public void setExamname(String _examname) {
		this.examname = _examname;
	}

	// get exam type
	public int getExamtype() {
		return examtype;
	}

	// set exam type
	public void setExamtype(int _examtype) {
		this.examtype = _examtype;
	}

	// get exam duration time
	public int getDurationofexam() {
		return durationofexam;
	}

	// get set exam duration time
	public void setDurationofexam(int _durationofexam) {
		this.durationofexam = _durationofexam;
	}

	// get no of question
	public int getNoofquestion() {
		return noofquestion;
	}

	// set no of question
	public void setNoofquestion(int _noofquestion) {
		this.noofquestion = _noofquestion;
	}

	// get question set
	public String getQuestionset() {
		return questionset;
	}

	// set question set
	public void setQuestionset(String _questionset) {
		this.questionset = _questionset;
	}

	// get date
	public String getDate() {
		return date;
	}

	// set date at which time function called
	public void setDate() {
		String _date;
		DateAndTime mDateAndTime = new DateAndTime();
		_date = mDateAndTime.getDate();
		this.date = _date;
	}

	// set date with parameters
	public void setDate(String _date) {
		this.date = _date;
	}

	// get time
	public String getTime() {
		return time;
	}

	// set time at which function called
	public void setTime() {
		String _time;
		DateAndTime mDateAndTime = new DateAndTime();
		_time = mDateAndTime.getTime();
		this.time = _time;
	}

	// set time with parameters
	public void setTime(String _time) {
		this.time = _time;
	}

	// get right answers
	public int getRightans() {
		return rightans;
	}

	// set right answers
	public void setRightans(int _rightans) {
		this.rightans = _rightans;
	}

	// get wrong answers
	public int getWrongans() {
		return wrongans;
	}

	// set wrong answers
	public void setWrongans(int _wrongans) {
		this.wrongans = _wrongans;
	}

	// get skip questions
	public int getSkipans() {
		return skipans;
	}

	// set skip questions
	public void setSkipans(int _skipans) {
		this.skipans = _skipans;
	}

	// get percentage
	public String getPercentage() {
		return percentage;
	}

	// set percentage
	public void setPercentage(String _percentage) {
		this.percentage = _percentage;
	}

}
