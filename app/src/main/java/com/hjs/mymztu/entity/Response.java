package com.hjs.mymztu.entity;

/**
 * 返回数据类型
 */
public class Response {
	
	private String retcode;//访问网络返回类型
	private String retdesc;//访问网络返回说明
	
	public String getRetdesc() {
		return retdesc;
	}
	public void setRetdesc(String retdesc) {
		this.retdesc = retdesc;
	}
	public String getRetcode() {
		return retcode;
	}
	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}
	
}
