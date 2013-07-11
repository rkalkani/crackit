package com.pvr.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dateandtime.DateAndTime;

public class ExamPaper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 64832669863608835L;
	private int totalQuestions = 0;
	private String questionSetName = null;
	private String questionSetVersion = null;
	private String author = null;
	private String subject = null;
	private String description = null;

	private int examId = 0;
	private String examname = null;
	private String examtype = null;
	private String date = null;
	private int durationofexam = 0;
	private int noofquestion = 0;
	private String questionset = null;

	private String attemptdate = null;
	private String attempttime = null;

	// For get date and time in custom extracted format
	DateAndTime dateandtime = new DateAndTime();

	private List<Question> questions = new ArrayList<Question>();

	public ExamPaper() {

	}

	public ExamPaper(int _totalQuestions, String _questionSetName,
			String _questionSetVersion, String _author, String _subject,
			String _description, int _examId, String _examname,
			String _examtype, String _date, int _durationofexam,
			int _noofquestion, String _questionset, List<Question> _questions) {
		this.totalQuestions = _totalQuestions;
		this.questionSetName = _questionSetName;
		this.questionSetVersion = _questionSetVersion;
		this.author = _author;
		this.subject = _subject;
		this.description = _description;

		this.examId = _examId;
		this.examname = _examname;
		this.examtype = _examtype;
		this.date = _date;
		this.durationofexam = _durationofexam;
		this.noofquestion = _noofquestion;
		this.questionset = _questionset;

		this.questions = _questions;

		this.attemptdate = this.getAttemptdate();
		this.attempttime = this.getAttempttime();
	}

	public ExamPaper(int _totalQuestions, String _questionSetName,
			String _questionSetVersion, String _author, String _subject,
			String _description, int _examId, String _examname,
			String _examtype, String _date, int _durationofexam,
			int _noofquestion, String _questionset) {
		this.totalQuestions = _totalQuestions;
		this.questionSetName = _questionSetName;
		this.questionSetVersion = _questionSetVersion;
		this.author = _author;
		this.subject = _subject;
		this.description = _description;

		this.examId = _examId;
		this.examname = _examname;
		this.examtype = _examtype;
		this.date = _date;
		this.durationofexam = _durationofexam;
		this.noofquestion = _noofquestion;
		this.questionset = _questionset;

		this.attemptdate = this.getAttemptdate();
		this.attempttime = this.getAttempttime();
	}

	public int getTotalQuestions() {
		return totalQuestions;
	}

	public void setTotalQuestions(int _totalQuestions) {
		this.totalQuestions = _totalQuestions;
	}

	private void incrementTotalquestions() {
		this.totalQuestions = this.totalQuestions + 1;
	}

	public String getQuestionSetName() {
		return questionSetName;
	}

	public void setQuestionSetName(String _questionSetName) {
		this.questionSetName = _questionSetName;
	}

	public String getQuestionSetVersion() {
		return questionSetVersion;
	}

	public void setQuestionSetVersion(String _questionSetVersion) {
		this.questionSetVersion = _questionSetVersion;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String _author) {
		this.author = _author;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String _subject) {
		this.subject = _subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String _description) {
		this.description = _description;
	}

	public int getExamId() {
		return examId;
	}

	public void setExamId(int _examId) {
		this.examId = _examId;
	}

	public String getExamname() {
		return examname;
	}

	public void setExamname(String _examname) {
		this.examname = _examname;
	}

	public String getExamtype() {
		return examtype;
	}

	public void setExamtype(String _examtype) {
		this.examtype = _examtype;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String _date) {
		this.date = _date;
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

	public String getAttemptdate() {
		return attemptdate;
	}

	public void setAttemptdate() {
		this.attemptdate = dateandtime.getDate();
	}

	public String getAttempttime() {
		return attempttime;
	}

	public void setAttempttime() {
		this.attempttime = dateandtime.getTime();
	}

	public Question getQuestion(int _index) {
		return questions.get(_index);
	}

	public void addQuestion(Question _question) {
		this.questions.add(_question);
		this.incrementTotalquestions();
	}

	public void setQuestion(int _index, Question _question) {
		this.questions.set(_index, _question);
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> _questions) {
		this.questions = _questions;
	}

}
