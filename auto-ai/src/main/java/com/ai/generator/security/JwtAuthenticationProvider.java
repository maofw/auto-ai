package com.ai.generator.security;

import com.ai.generator.util.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 身份验证提供者
 * @author Louis
 * @date Nov 20, 2018
 */
public class JwtAuthenticationProvider extends DaoAuthenticationProvider {

    public JwtAuthenticationProvider(UserDetailsService userDetailsService) {
        setUserDetailsService(userDetailsService);
        setHideUserNotFoundExceptions(false);
    }

    @Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			logger.debug("Authentication failed: no credentials provided");
			throw new AuthenticationCredentialsNotFoundException("密码不能为空");
		}
		//用户填写密码
		String presentedPassword = authentication.getCredentials().toString();
		String salt = ((JwtUserDetails) userDetails).getSalt();
		// 覆写密码验证逻辑
		if (!new PasswordEncoder(salt).matches(userDetails.getPassword(), presentedPassword)) {
			logger.debug("Authentication failed: password does not match stored value");
			throw new BadCredentialsException("密码不正确");
		}
		//密码验证成功了
	}

}