/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.common.exception;

/**
 * 自定义异常
 */
public class MyException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code = 500;
    
    public MyException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public MyException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public MyException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public MyException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
