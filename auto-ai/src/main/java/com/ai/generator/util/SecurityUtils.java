package com.ai.generator.util;

import com.ai.generator.model.SysUser;
import com.ai.generator.security.JwtAuthenticatioToken;
import com.ai.generator.security.JwtUserDetails;
import com.alibaba.fastjson.JSON;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

/**
 * Security相关操作
 * @author Louis
 * @date Nov 20, 2018
 */
public class SecurityUtils {

	/**
	 * 系统登录认证
	 * @param request
	 * @param username
	 * @param password
	 * @param authenticationManager
	 * @return
	 */
	public static String login(HttpServletRequest request, String username, String password, AuthenticationManager authenticationManager) throws AuthenticationException {
		JwtAuthenticatioToken token = new JwtAuthenticatioToken(username, password);
		token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		// 执行登录认证过程
	    Authentication authentication = authenticationManager.authenticate(token);
	    // 认证成功存储认证信息到上下文
	    SecurityContextHolder.getContext().setAuthentication(authentication);
		// 生成令牌并返回给客户端
	   return JwtTokenUtils.generateToken(authentication,username);
	}

	/**
	 * 获取令牌进行认证
	 * @param request
	 */
	public static void checkAuthentication(HttpServletRequest request,AuthenticationManager authenticationManager) throws Exception {
		// 获取令牌并根据令牌获取登录认证信息
		JwtTokenUtils.getAuthenticationeFromToken(request,authenticationManager);
		// 设置登录认证信息到上下文
	}

	public static String getUsernameByToken(String token){
		return JwtTokenUtils.getUsernameFromToken(token);
	}
	/**
	 * 获取当前用户名
	 * @return
	 */
	public static String getUsername() {
		return getUsername(getAuthentication());
	}
	
	/**
	 * 获取用户名
	 * @return
	 */
	public static String getUsername(Authentication authentication) {
		String username = null;
		if(authentication != null) {
			Object principal = authentication.getPrincipal();
			if(principal != null ){
			   if(principal instanceof UserDetails) {
				   username = ((UserDetails) principal).getUsername();
			   }else if(principal instanceof String) {
				   username= (String)principal ;
			   }
			}
		}
		return username;
	}
	/**
	 * 获取当前用户
	 * @return
	 */
	public static SysUser getCurrentUser() {
		SysUser user = null;
		Authentication authentication = getAuthentication();
		if(authentication != null ) {
			Object principal = authentication.getPrincipal();
			if(principal != null ){
				if(principal instanceof JwtUserDetails) {
					user = ((JwtUserDetails) principal).getUser() ;
				}
			}
			System.out.println("========user："+ (user==null?"null":JSON.toJSONString(user)));
		}
		return user;
	}

	/**
	 * 获取当前登录信息
	 * @return
	 */
	public static Authentication getAuthentication() {
		if(SecurityContextHolder.getContext() == null) {
			return null;
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication;
	}
	
}
