package com.example.service.impl;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.constant.CommonConstant;
import com.example.mapper.UserMapper;
import com.example.model.entity.User;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    
    @Resource
    private StringRedisTemplate redisTemplate;
    
    
    @Override
    public User getLoginUser(String token) {
        Long id = (Long) JWTUtil.parseToken(token).getPayload("id");
        String userStr = redisTemplate.opsForValue().get(CommonConstant.USER_CACHE_KEY);
        if (StringUtils.isNotEmpty(userStr)) {
            return JSONUtil.toBean(userStr, User.class);
        }
        return this.getById(id);
    }
}
