package com.example.controller;

import cn.hutool.jwt.JWTUtil;
import com.example.common.BaseResponse;
import com.example.common.ResultUtils;
import com.example.constant.CommonConstant;
import com.example.model.dto.LoginDTO;
import com.example.model.entity.User;
import com.example.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 权限校验
 *
 * @author ther
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    
    @Resource
    private UserService userService;
    
    @PostMapping("/login")
    public BaseResponse login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            return ResultUtils.error(40000, "参数错误");
        }
        User user = userService.login(username, password);
        if (user == null) {
            return ResultUtils.error(40000, "用户名或密码错误");
        }
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", user.getId());
        String accessToken = JWTUtil.createToken(payload, CommonConstant.ACCESS_TOKEN.getBytes());
        String refreshToken = JWTUtil.createToken(payload, CommonConstant.REFRESH_TOKEN.getBytes());
        response.addCookie(new Cookie("jwt", refreshToken));
        return ResultUtils.success(accessToken);
    }
}
