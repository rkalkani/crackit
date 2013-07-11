package com.pvr.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2848042257316037077L;
	private String qid = null;
	private String question = null;
	private List<String> option = new ArrayList<String>();
	private String image = null;
	private int ans = 0;
	private int userans = 0;

	public Question() {

	}

	public Question(String _qid, String _question, List<String> _option,
			String _image, int _ans) {
		this.qid = _qid;
		this.question = _question;
		this.option = _option;
		this.image = _image;
		this.ans = _ans;
		this.userans = 0;
	}

	public Question(String _qid, String _question, List<String> _option,
			String _image, int _ans, int _userans) {
		this.qid = _qid;
		this.question = _question;
		this.option = _option;
		this.image = _image;
		this.ans = _ans;
		this.userans = _userans;
	}

	public String getQid() {
		return qid;
	}

	public void setQid(String _qid) {
		this.qid = _qid;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String _question) {
		this.question = _question;
	}

	public List<String> getOptions() {
		return option;
	}

	public void setOptions(List<String> _option) {
		this.option = _option;
	}

	public String getOption(int _index) {
		return option.get(_index);
	};

	public void addOption(String _option) {
		this.option.add(_option);
	}

	public void setOption(int _index, String _option) {
		this.option.set(_index, _option);
	}

	public String getImage() {
		return image;
	}

	public void setImage(String _image) {
		this.image = _image;
	}

	public int getAns() {
		return ans;
	}

	public void setAns(int _ans) {
		this.ans = _ans;
	}

	public int getUserans() {
		return userans;
	}

	public void setUserans(int _userans) {
		this.userans = _userans;
	}

}
