package com.auth.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class StringFormatUtil {

	public static String joinFormat(final String separator, final Object... objects) {
        return joinFormat(separator, false, false, objects);
    }
    
    /**
     * 合并字符串
     * @param separator 分隔符
     * @param isHead 字符串前是否加分隔符
     * @param isEnd 字符串后是否加分隔符
     * @param objects 要合并的字符串
     */
    public static String joinFormat(final String separator, boolean isHead, boolean isEnd, final Object... objects) {
        if(Objects.isNull(objects)) {
            return separator;
        }
        
        if(Objects.isNull(separator)) {
            return Arrays.asList(objects).stream().map(v -> Objects.toString(v, "")).collect(Collectors.joining());
        }
        
        StringBuilder builder = new StringBuilder();
        if(isHead) {
            builder.append(separator);
        }
        
        for (int i = 0; i < objects.length; i++) {
            //builder.append(String.format("%s" + separator, Objects.toString(objects[i], "")));
            builder.append(Objects.toString(objects[i], "")).append(separator);
        }
        
        if(!isEnd) {
            builder.deleteCharAt(builder.lastIndexOf(separator));
        }
        return builder.toString();
    }
    
    /**
     * 字符串首字母大写
     */
	public static String toUpperCaseFirstOne(String str) {
		if (Character.isUpperCase(str.charAt(0))) {
			return str;
		}
		return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
	}
	
	/**
	 * 字符串首字母小写
	 */
	public static String toLowerCaseFirstOne(String str) {
		if (Character.isLowerCase(str.charAt(0))) {
			return str;
		}
		return (new StringBuilder()).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
	}
	
}
