package com.company.musicscore.model;

import android.content.ContentValues;

public class ExamScore {

	public ExamScore(String id, String judgeid, String studentid,
			String worksname, String score, String techablity,
			String musicperform, String voicecondition, String execution,
			String difficulity, String createdate, String deviceno) {
		this.id = id;
		this.judgeid = judgeid;
		this.studentid = studentid;
		this.worksname = worksname;
		this.score = score;
		this.techability = techablity;
		this.musicperform = musicperform;
		this.voicecondition = voicecondition;
		this.execution = execution;
		this.difficulty = difficulity;
		this.createdate = createdate;
		this.deviceno = deviceno;
	}
	public ExamScore() {
		// TODO Auto-generated constructor stub
	}
	
	String id;
	String judgeid;
	String studentid;
	String worksname;
	String score;
	String techability;
	String musicperform;
	String voicecondition;
	String execution;
	String difficulty;
	String  createdate;
	String deviceno;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return the judgeid
	 */
	public String getJudgeid() {
		return judgeid;
	}
	/**
	 * @param judgeid the judgeid to set
	 */
	public void setJudgeid(String judgeid) {
		this.judgeid = judgeid;
	}
	/**
	 * @return the studentid
	 */
	public String getStudentid() {
		return studentid;
	}
	/**
	 * @param studentid the studentid to set
	 */
	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}
	/**
	 * @return the worksname
	 */
	public String getWorksname() {
		return worksname;
	}
	/**
	 * @param worksname the worksname to set
	 */
	public void setWorksname(String worksname) {
		this.worksname = worksname;
	}
	/**
	 * @return the score
	 */
	public String getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(String score) {
		this.score = score;
	}
	/**
	 * @return the techablity
	 */
	public String getTechablity() {
		return techability;
	}
	/**
	 * @param techablity the techablity to set
	 */
	public void setTechablity(String techablity) {
		this.techability = techablity;
	}
	/**
	 * @return the musicperform
	 */
	public String getMusicperform() {
		return musicperform;
	}
	/**
	 * @param musicperform the musicperform to set
	 */
	public void setMusicperform(String musicperform) {
		this.musicperform = musicperform;
	}
	/**
	 * @return the voicecondition
	 */
	public String getVoicecondition() {
		return voicecondition;
	}
	/**
	 * @param voicecondition the voicecondition to set
	 */
	public void setVoicecondition(String voicecondition) {
		this.voicecondition = voicecondition;
	}
	/**
	 * @return the execution
	 */
	public String getExecution() {
		return execution;
	}
	/**
	 * @param execution the execution to set
	 */
	public void setExecution(String execution) {
		this.execution = execution;
	}
	/**
	 * @return the difficulity
	 */
	public String getDifficulity() {
		return difficulty;
	}
	/**
	 * @param difficulity the difficulity to set
	 */
	public void setDifficulity(String difficulity) {
		this.difficulty = difficulity;
	}
	/**
	 * @return the createdate
	 */
	public String getCreatedate() {
		return createdate;
	}
	/**
	 * @param createdate the createdate to set
	 */
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	/**
	 * @return the deviceno
	 */
	public String getDeviceno() {
		return deviceno;
	}
	/**
	 * @param deviceno the deviceno to set
	 */
	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}
	
	public ContentValues getContentValues()
	{
		ContentValues values=new ContentValues();
		values.put("JUDGEID", judgeid);
		values.put("STUDENTID", studentid);
		values.put("WORKSNAME", worksname);
		values.put("TECHABILITY", techability);
		values.put("MUSICPERFORM", musicperform);
		values.put("VOICECONDITION", voicecondition);
		values.put("EXECUTION", execution);
		values.put("DIFFICULTYDEGREE", difficulty);
		values.put("CREATEDATE", createdate);
		values.put("DEVICENO", deviceno);
		return values;
		
	}

}
