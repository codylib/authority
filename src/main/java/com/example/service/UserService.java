package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.User;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface UserService extends IService<User> {
    
    /**
     * 获取当前登录用户
     *
     * @param token
     * @return
     */
    User getLoginUser(String token);
    
    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    String login(String username, String password, HttpServletResponse response);
    
    /**
     * 刷新accessToken
     * @param accessToken
     * @return
     */
    String refresh(String accessToken);
}
