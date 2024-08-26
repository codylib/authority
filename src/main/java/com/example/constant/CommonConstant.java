package com.example.constant;

/**
 * 通用常量
 *
 * @author ther
 */
public interface CommonConstant {
    
    /**
     * 认证请求头
     */
    String AUTH_HEADER = "Authorization";
    
    /**
     * Basic Token 前缀
     */
    String AUTH_TYPE = "Bearer ";
    
    /**
     * 访问令牌
     */
    String ACCESS_TOKEN = "accessToken";
    
    /**
     * 刷新令牌
     */
    String REFRESH_TOKEN = "refreshToken";
    
    /**
     * 用户信息缓存 key
     */
    String USER_CACHE_KEY = "login:user";
    
    /**
     * 刷新token缓存 key
     */
    String REFRESH_CACHE_KEY = "login:refresh";

    /**
     * 升序
     */
    String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    String SORT_ORDER_DESC = " descend";
}
