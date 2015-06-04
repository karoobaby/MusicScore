package com.company.musicscore.model;

import android.content.ContentValues;

public class Player {

	public Player(String id, String cname, String idcard, String zyfclass,
			String zysclass, String photopath, String musics,String indexType,int status) {
		this.id = id;
		this.cname = cname;
		this.idcard = idcard;
		this.zyfclass = zyfclass;
		this.zysclass = zysclass;
		this.photopath = photopath;
		this.musics = musics;
		this.indexType=indexType;
		this.status=status;
	}

	public Player() {
		// TODO Auto-generated constructor stub
	}
	
	
	private String id;
	private String cname;
	private String idcard;
	private String zyfclass;
	private String zysclass;
	private String photopath;
	private String musics;
	private String indexType;
	private int status;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the cname
	 */
	public String getCname() {
		return cname;
	}
	/**
	 * @param cname the cname to set
	 */
	public void setCname(String cname) {
		this.cname = cname;
	}
	/**
	 * @return the idcard
	 */
	public String getIdcard() {
		return idcard;
	}
	/**
	 * @param idcard the idcard to set
	 */
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	/**
	 * @return the zyfclass
	 */
	public String getZyfclass() {
		return zyfclass;
	}
	/**
	 * @param zyfclass the zyfclass to set
	 */
	public void setZyfclass(String zyfclass) {
		this.zyfclass = zyfclass;
	}
	/**
	 * @return the zysclass
	 */
	public String getZysclass() {
		return zysclass;
	}
	/**
	 * @param zysclass the zysclass to set
	 */
	public void setZysclass(String zysclass) {
		this.zysclass = zysclass;
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
	/**
	 * @return the musics
	 */
	public String getMusics() {
		return musics;
	}
	/**
	 * @param musics the musics to set
	 */
	public void setMusics(String musics) {
		this.musics = musics;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ContentValues getContentValues()
	{
		ContentValues values=new ContentValues();
		values.put("_id", id);
		values.put("CNAME", cname);
		values.put("IDCARD",idcard);
		values.put("ZYFCLASS", zyfclass);
		values.put("ZYSCLASS", zysclass);
		values.put("PHOTOPATH", photopath);
		values.put("MUSICS", musics);
		values.put("INDEXTYPE", indexType);
		values.put("STATUS",status);
		return values;
	}

	/**
	 * @return indexType
	 */
	public String getIndexType() {
		return indexType;
	}

	/**
	 * @param indexType 要设置的 indexType
	 */
	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}
	
}
