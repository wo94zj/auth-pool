package com.auth.enums;

public enum AppNameEnums {

	xwk;
	
	public final static String APPNAME = "appname";
	public final static String DEFAULT_APPNAME = AppNameEnums.xwk.name();
	
	
	public static boolean isValid(String appname) {
		for (AppNameEnums app : AppNameEnums.values()) {
			if(app.name().equals(appname)) {
				return true;
			}
		}
		
		return false;
	}
}
