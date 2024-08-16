package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.User;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;


public interface UserService extends IService<User> {
    
    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(String token);
}
