package com.wjclovejava.demo.mini.api.controller;

import com.wjclovejava.demo.common.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wjc
 * @Description:
 * @Date: created in 2019/2/22 9:29
 */
@RestController
public class BasicController {

    @Autowired
    public RedisOperator redis;

    public static final String USER_REDIS_SESSION="user-redis-session";
    /**
     * 每页分页的数量
     */
    public static final Integer PAGE_SIZE =5;
}
