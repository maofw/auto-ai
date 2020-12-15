package com.ai.generator.util;

import java.util.UUID;

/**
 * 密码工具类
 * @author Louis
 * @date Sep 1, 2018
 */
public class PasswordUtils {
	/**
	 * 密码验证 必须是6-20位的字母、数字、下划线（这里字母、数字、下划线是指任意组合，没有必须三类均包含）
	 * @param pwd
	 * @return
	 */
	public static boolean checkPwd(String pwd) {
		String regExp = "^[\\w_]{6,20}$";
		if(pwd.matches(regExp)) {
			return true;
		}
		return false;
	}

	/**
	 * 匹配密码
	 * @param salt 盐
	 * @param rawPass 明文 
	 * @param encPass 密文
	 * @return
	 */
	public static boolean matches(String salt, String rawPass, String encPass) {
		return new PasswordEncoder(salt).matches(encPass, rawPass);
	}
	
	/**
	 * 明文密码加密
	 * @param rawPass 明文
	 * @param salt
	 * @return
	 */
	public static String encode(String rawPass, String salt) {
		return new PasswordEncoder(salt).encode(rawPass);
	}

	/**
	 * 获取加密盐
	 * @return
	 */
	public static String getSalt() {
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
	}
}
