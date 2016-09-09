package com.hjs.mymztu.entity;

import com.google.gson.Gson;

public class MzituBean {

	private String title;
	private String[] urls;
	private String tags;
	private String firstImgUrl;

	public String getTitle() {
		return title==null?"":title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String[] getUrls() {
		return urls;
	}
	public void setUrls(String[] urls) {
		this.urls = urls;
	}
	public String getTags() {
		return tags==null?"":tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getFirstImgUrl() {
		return firstImgUrl==null?"":firstImgUrl;
	}
	public void setFirstImgUrl(String firstImgUrl) {
		this.firstImgUrl = firstImgUrl;
	}
	
	public static String getJsonString(MzituBean bean){
		return new Gson().toJson(bean);
	}
	
}
