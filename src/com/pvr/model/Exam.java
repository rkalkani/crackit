package com.pvr.model;

import java.io.Serializable;

public class Exam implements Serializable {

	// private variable

	/**
	 * for hold exam details
	 */
	private static final long serialVersionUID = 101331;
	private int id = 0;
	private String examname = null;
	private int examtype = 0;
	private String date = null;
	private String time = null;
	private int durationofexam = 0;
	private int noofquestion = 0;
	private String questionset = null;

	// Empty Constructor
	public Exam() {
		// Default Constructor
	}

	// Constructor
	public Exam(int _id, String _examname, int _examtype, String _date,
			String _time, int _durationofexam, int _noofquestion,
			String _questionset) {
		this.id = _id;
		this.examname = _examname;
		this.examtype = _examtype;
		this.date = _date;
		this.time = _time;
		this.durationofexam = _durationofexam;
		this.noofquestion = _noofquestion;
		this.questionset = _questionset;
	}

	// Constructor
	public Exam(String _examname, int _examtype, String _date, String _time,
			int _durationofexam, int _noofquestion, String _questionset) {
		this.examname = _examname;
		this.examtype = _examtype;
		this.date = _date;
		this.time = _time;
		this.durationofexam = _durationofexam;
		this.noofquestion = _noofquestion;
		this.questionset = _questionset;
	}

	// Getting ID
	public int getId() {
		return this.id;
	}

	// Setting ID
	public void setId(int _id) {
		this.id = _id;
	}

	// Getting Exam name
	public String getExamname() {
		return this.examname;
	}

	// Setting Exam name
	public void setExamname(String _examname) {
		this.examname = _examname;
	}

	// Getting Exam type
	public int getExamtype() {
		return this.examtype;
	}

	// Setting Exam type
	public void setExamtype(int _examtype) {
		this.examtype = _examtype;
	}

	// Getting Date
	public String getDate() {
		return this.date;
	}

	// Setting Date
	public void setDate(String _date) {
		this.date = _date;
	}

	// Getting Time
	public String getTime() {
		return this.time;
	}

	// Setting Time
	public void setTime(String _time) {
		this.time = _time;
	}

	// Getting Duration of exam
	public int getDurationofexam() {
		return this.durationofexam;
	}

	// Setting Duration of exam
	public void setDurationofexam(int _durationofexam) {
		this.durationofexam = _durationofexam;
	}

	// Getting no of question
	public int getNoofquestion() {
		return this.noofquestion;
	}

	// Setting no of question
	public void setNoofquestion(int _nofoquestion) {
		this.noofquestion = _nofoquestion;
	}

	// Getting question set
	public String getQuestionset() {
		return this.questionset;
	}

	// Setting question set
	public void setQuestionset(String _questionset) {
		this.questionset = _questionset;
	}

}
