package com.company.musicscore.model;

import android.content.ContentValues;

public class DeviceSet {

	public DeviceSet() {
		// TODO Auto-generated constructor stub
	}
	
	public DeviceSet(String id, String dno, String dname, String ename,
			String judgeid, String jname, String workdept, String photopath) {
		this.id = id;
		this.dno = dno;
		this.dname = dname;
		this.ename = ename;
		this.judgeid = judgeid;
		this.jname = jname;
		this.workdept = workdept;
		this.photopath = photopath;
	}

	String id;
	String dno;
	String dname;
	String ename;
	String judgeid;
	String jname;
	String workdept;
	String photopath;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the dno
	 */
	public String getDno() {
		return dno;
	}

	/**
	 * @param dno the dno to set
	 */
	public void setDno(String dno) {
		this.dno = dno;
	}

	/**
	 * @return the dname
	 */
	public String getDname() {
		return dname;
	}
	
	public void setId(String id)
	{
		this.id=id;
	}

	/**
	 * @param dname the dname to set
	 */
	public void setDname(String dname) {
		this.dname = dname;
	}

	/**
	 * @return the ename
	 */
	public String getEname() {
		return ename;
	}

	/**
	 * @param ename the ename to set
	 */
	public void setEname(String ename) {
		this.ename = ename;
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
	 * @return the jname
	 */
	public String getJname() {
		return jname;
	}

	/**
	 * @param jname the jname to set
	 */
	public void setJname(String jname) {
		this.jname = jname;
	}

	/**
	 * @return the workdept
	 */
	public String getWorkdept() {
		return workdept;
	}

	/**
	 * @param workdept the workdept to set
	 */
	public void setWorkdept(String workdept) {
		this.workdept = workdept;
	}

	/**
	 * @return the photopath
	 */
	public String getPhotopath() {
		return photopath;
	}

	/**
	 * @param photopath the photopath to set
	 */
	public void setPhotopath(String photopath) {
		this.photopath = photopath;
	}
	
	public ContentValues	getContentValues()
	{
		ContentValues values=new ContentValues();
		values.put("_id", id);
		values.put("DNO", dno);
		values.put("DNAME", dname);
		values.put("ENAME", ename);
		values.put("JUDGEID", judgeid);
		values.put("JNAME", jname);
		values.put("WORKDEPT", workdept);
		values.put("PHOTOPATH",photopath);
		return values;
	}
}
