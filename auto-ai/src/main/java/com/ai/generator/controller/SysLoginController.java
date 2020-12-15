package com.ai.generator.controller;

import com.ai.generator.http.HttpResult;
import com.ai.generator.model.SysUser;
import com.ai.generator.model.request.LoginBeanRequest;
import com.ai.generator.util.SecurityUtils;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器
 *
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
public class SysLoginController {

    @Autowired
    private Producer producer;

    @Autowired
    private AuthenticationManager authenticationManager;
//
//    @GetMapping("captcha.jpg")
//    public void captcha(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
//        response.setHeader("Cache-Control", "no-store, no-cache");
//        response.setContentType("image/jpeg");
//
//        // 生成文字验证码
//        String text = producer.createText();
//        // 生成图片验证码
//        BufferedImage image = producer.createImage(text);
//        // 保存到验证码到 session
//        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, text);
//
//        ServletOutputStream out = response.getOutputStream();
//        ImageIO.write(image, "jpg", out);
//        if (out != null) {
//            try {
//                out.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * 登录接口
     */
    @PostMapping(value = "/login")
    public HttpResult login(@RequestBody LoginBeanRequest loginBean, HttpServletRequest request) {
        String username = loginBean.getAccount();
        String password = loginBean.getPassword();
        // 系统登录认证
        try {
            String token = SecurityUtils.login(request, username, password, authenticationManager);
            SysUser user = SecurityUtils.getCurrentUser();
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("name", user.getName());
            map.put("deptId", user.getDeptId());
            map.put("deptName", user.getDeptName());
            map.put("email",user.getEmail());
            map.put("mobile", user.getMobile());
            map.put("token", token);
            return HttpResult.success(map);
        }catch (AuthenticationException ex){
            ex.printStackTrace();
            return HttpResult.error(ex.getMessage());
        }
    }
}
