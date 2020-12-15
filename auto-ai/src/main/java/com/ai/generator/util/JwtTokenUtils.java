package com.ai.generator.util;

import com.ai.generator.model.SysUser;
import com.ai.generator.security.GrantedAuthorityImpl;
import com.ai.generator.security.JwtAuthenticatioToken;
import com.ai.generator.security.JwtUserDetails;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

/**
 * JWT工具类
 * @author Louis
 * @date Nov 20, 2018
 */
public class JwtTokenUtils implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户名称
	 */
	private static final String USERNAME = Claims.SUBJECT;

	private static final String USER = "user";
	private static final String DOCTOR = "doctor";
	/**
	 * 创建时间
	 */
	private static final String CREATED = "created";
	/**
	 * 权限列表
	 */
	private static final String AUTHORITIES = "authorities";
	/**
     * 密钥
     */
    private static final String SECRET = "abcdefgh";
    /**
     * 有效期12小时
     */
    private static final long EXPIRE_TIME = 12 * 60 * 60 * 1000;

    /**
	 * 生成令牌
	 *
	 * @param authentication
	 * @return 令牌
	 */
	public static String generateToken(Authentication authentication,String username) {
	    Map<String, Object> claims = new HashMap<>();
	    claims.put(USERNAME, username);
	    claims.put(CREATED, new Date());
	    claims.put(AUTHORITIES, authentication.getAuthorities());
		SysUser sysUser = SecurityUtils.getCurrentUser();
		if(sysUser!=null){
			SysUser sysUser2 = new SysUser();
			try {
				BeanUtils.copyProperties(sysUser2,sysUser);
				sysUser2.setPassword(null);
				sysUser2.setSalt(null);
				sysUser2.setOldPassword(null);
				claims.put(USER,sysUser2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	    return generateToken(claims);
	}

	/**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private static String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    /**
	 * 从令牌中获取用户名
	 *
	 * @param token 令牌
	 * @return 用户名
	 */
	public static String getUsernameFromToken(String token) {
	    String username;
	    try {
	        Claims claims = getClaimsFromToken(token);
	        username = claims.getSubject();
	    } catch (Exception e) {
	        username = null;
	    }
	    return username;
	}
	
	/**
	 * 根据请求令牌获取登录认证信息
	 * @param request
	 * @return 用户名
	 */
	public static Authentication getAuthenticationeFromToken(HttpServletRequest request, AuthenticationManager authenticationManager) throws Exception {
		Authentication authentication = null;
		// 获取请求携带的令牌
		String token = JwtTokenUtils.getToken(request);
		if(token != null) {
			// 请求令牌不能为空
 			if(SecurityUtils.getAuthentication() == null) {
				// 上下文中Authentication为空
				Claims claims = getClaimsFromToken(token);
				if(claims == null) {
					throw new Exception("TOKEN校验失败");
				}
				String username = claims.getSubject();
				if(username == null) {
					throw new Exception("TOKEN信息有误");
				}
				if(isTokenExpired(token)) {
					throw new Exception("TOKEN已失效，请重新登录");
				}
				Object authors = claims.get(AUTHORITIES);
				List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
				if (authors != null && authors instanceof List) {
					for (Object object : (List) authors) {
						authorities.add(new GrantedAuthorityImpl((String) ((Map) object).get("authority")));
					}
				}
				//查询医生
				Object userObj =  claims.get(USER);
                SysUser user = null ;
                if(userObj!=null){
                    user = JSON.parseObject(JSON.toJSONString(userObj),SysUser.class);
                }

				JwtUserDetails jwtUserDetails = new JwtUserDetails(username, null, null, authorities,user);
				JwtAuthenticatioToken jwtAuthenticatioToken =  new JwtAuthenticatioToken(jwtUserDetails, null, authorities, token);
				jwtAuthenticatioToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(jwtAuthenticatioToken);
			} else {
				String currentUser = SecurityUtils.getUsername();
				if(validateToken(token, currentUser)) {
					// 如果上下文中Authentication非空，且请求令牌合法，直接返回当前登录认证信息
					authentication = SecurityUtils.getAuthentication();
				}else{
					SecurityContextHolder.getContext().setAuthentication(null);
				}
			}

		}
		return authentication;
	}

	/**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
	 * 验证令牌
	 * @param token
	 * @param username
	 * @return
	 */
	public static Boolean validateToken(String token, String username) {
	    String userName = getUsernameFromToken(token);
	    return (userName.equals(username) && !isTokenExpired(token));
	}

	/**
	 * 刷新令牌
	 * @param token
	 * @return
	 */
	public static String refreshToken(String token) {
	    String refreshedToken;
	    try {
	        Claims claims = getClaimsFromToken(token);
	        claims.put(CREATED, new Date());
	        refreshedToken = generateToken(claims);
	    } catch (Exception e) {
	        refreshedToken = null;
	    }
	    return refreshedToken;
	}

	/**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期 是否过期 true-过期 false-未过期
     */
    public static Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取请求token
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
    	String token = request.getHeader("Authorization");
        String tokenHead = "Bearer ";
        if(token == null) {
        	token = request.getHeader("token");
        } else if(token.contains(tokenHead)){
        	token = token.substring(tokenHead.length());
        } 
        if("".equals(token)) {
        	token = null;
        }
        return token;
    }

}