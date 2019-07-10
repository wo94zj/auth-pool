package com.auth.util;

import java.util.UUID;

public class SecretUtil {

	/**
	 * 随机生成盐
	 */
    public static String salt() {
        return MD5Util.md5("salt-" + System.currentTimeMillis() + "-" + UUID.randomUUID());
    }
}
