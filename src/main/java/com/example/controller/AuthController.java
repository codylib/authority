package com.example.controller;

import cn.hutool.jwt.JWTUtil;
import com.example.common.BaseResponse;
import com.example.common.ResultUtils;
import com.example.constant.CommonConstant;
import com.example.model.dto.LoginDTO;
import com.example.model.entity.User;
import com.example.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

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
        String accessToken = userService.login(username, password, response);
        if (accessToken == null) {
            return ResultUtils.error(40000, "用户名或密码错误");
        }
        Map<String, Object> res = new HashMap<>();
        res.put("accessToken", accessToken);
        return ResultUtils.success(res);
    }
    
    @PostMapping("/refresh")
    public BaseResponse refresh(@RequestHeader("Authorization") String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return ResultUtils.error(40000, "参数错误");
        }
        String newToken = userService.refresh(accessToken);
        if (newToken == null) {
            return ResultUtils.error(40000, "token已过期");
        }
        return ResultUtils.success(newToken);
    }
}
