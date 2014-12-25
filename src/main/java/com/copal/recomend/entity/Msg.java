package com.copal.recomend.entity;

import java.util.List;

public class Msg {

	private String info;
	
	private int code;
	
	private List<String> result;

	public Msg() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Msg(String info, int code, List<String> result) {
		super();
		this.info = info;
		this.code = code;
		this.result = result;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<String> getResult() {
		return result;
	}

	public void setResult(List<String> result) {
		this.result = result;
	}
	
	
	
}
