package com.auth.exception;

public class CommonException extends RuntimeException {

	private static final long serialVersionUID = 9167533023261028788L;
	
	private Integer code;

	public CommonException(Integer code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public CommonException(Integer code, String message) {
		this(code, message, null);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
