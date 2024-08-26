package com.example.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.constant.CommonConstant;
import com.example.mapper.UserMapper;
import com.example.model.entity.User;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Resource
    private UserMapper userMapper;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Value("${password.salt}")
    private String salt;
    
    
    @Override
    public User getLoginUser(String token) {
        Long id = (Long) JWTUtil.parseToken(token).getPayload("id");
        String userStr = redisTemplate.opsForValue().get(CommonConstant.USER_CACHE_KEY);
        if (StringUtils.isNotEmpty(userStr)) {
            return JSONUtil.toBean(userStr, User.class);
        }
        return this.getById(id);
    }
    
    @Override
    public String login(String username, String password, HttpServletResponse response) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        String accessToken = null;
        if (user != null) {
            String encryptPassword = new HMac(HmacAlgorithm.HmacSHA256, salt.getBytes()).digestBase64(password, false);
            if (!user.getPassword().equals(encryptPassword)) {
                return null;
            }
            redisTemplate.opsForValue().set(CommonConstant.USER_CACHE_KEY + ":" + user.getId(), JSONUtil.toJsonStr(user), 1, TimeUnit.DAYS);
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", user.getId());
            accessToken = JWTUtil.createToken(payload, CommonConstant.ACCESS_TOKEN.getBytes());
            String refreshToken = JWTUtil.createToken(payload, CommonConstant.REFRESH_TOKEN.getBytes());
            response.addCookie(new Cookie("jwt", refreshToken));
            redisTemplate.opsForValue().set(CommonConstant.REFRESH_CACHE_KEY + ":" + user.getId(), refreshToken, 1, TimeUnit.DAYS);
        }
        return accessToken;
    }
    
    @Override
    public String refresh(String accessToken) {
        Long id = NumberUtil.parseLong(JWTUtil.parseToken(accessToken).getPayload("id").toString());
        String refreshToken = redisTemplate.opsForValue().get(CommonConstant.REFRESH_CACHE_KEY + ":" + id);
        if (StringUtils.isBlank(refreshToken)) {
            return null;
        }
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", id);
        return JWTUtil.createToken(payload, CommonConstant.ACCESS_TOKEN.getBytes());
    }
}
