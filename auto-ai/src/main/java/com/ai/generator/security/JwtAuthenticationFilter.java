package com.ai.generator.security;

import com.ai.generator.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录认证过滤器
 * @author Louis
 * @date Nov 20, 2018
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

	
	@Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //用户登录了，需要验证用户权限是否正常 token是否有效
        try {
            SecurityUtils.checkAuthentication(request,getAuthenticationManager());
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            //授权失败了返回403状态码
            response.setStatus(403);
        }

    }
    
}