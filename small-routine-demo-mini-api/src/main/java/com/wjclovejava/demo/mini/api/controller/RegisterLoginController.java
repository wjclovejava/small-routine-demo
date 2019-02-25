package com.wjclovejava.demo.mini.api.controller;

import com.wjclovejava.demo.common.utils.BasicResult;
import com.wjclovejava.demo.common.utils.DozerUtils;
import com.wjclovejava.demo.common.utils.MD5Utils;
import com.wjclovejava.demo.mini.api.controller.vo.UsersVO;
import com.wjclovejava.demo.pojo.Users;
import com.wjclovejava.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @Author: wjc
 * @Description:
 * @Date: created in 2019/2/21 11:46
 */
@Api(value = "注册登录控制器", tags = "注册登录业务的Controller")
@RestController
public class RegisterLoginController extends BasicController{

    @Autowired
    private UserService userService;


    @ApiOperation(value = "用户注册",notes = "用户注册的接口")
    @PostMapping("/register")
    public BasicResult register(@RequestBody Users user) {
        //1.判断用户名密码不为空
        if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())){
            return BasicResult.errorMsg("用户名和密码不能为空");
        }

        //2.用户名是否存在
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());
        if(!usernameIsExist){
            try {
                user.setNickname(user.getUsername());
                user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
                user.setFansCounts(0);
                user.setFollowCounts(0);
                user.setReceiveLikeCounts(0);
                userService.saveUser(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            return BasicResult.errorMsg("用户名已存在");
        }
        user.setPassword(null);

        UsersVO userVO = getUserRedisSessionToken(user);
        return BasicResult.ok(userVO);
    }

    @ApiOperation(value = "用户登录",notes = "用户登录的接口")
    @PostMapping("/login")
    public BasicResult login(@RequestBody Users user) {
        String username=user.getUsername();
        String password=user.getPassword();

        //1.判断用户名密码不为空
        if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())){
            return BasicResult.errorMsg("用户名和密码不能为空");
        }
        //2.用户名是否存在
        Users userResult = null;
        try {
            userResult = userService.queryUserForLogin(username,MD5Utils.getMD5Str(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userResult != null) {
            userResult.setPassword(null);
            UsersVO userVO = getUserRedisSessionToken(userResult);
            return BasicResult.ok(userVO);
        }else {
            return BasicResult.errorMsg("用户名或者密码不正确!");
        }
    }

    @ApiOperation(value = "用户注销",notes = "用户注销的接口")
    @PostMapping("/logout")
    public BasicResult logout(String userId) {
        redis.del(USER_REDIS_SESSION+":"+userId);
        return BasicResult.ok();
    }

    /**
     * 设置user Token
     * @param user
     * @return
     */
    public UsersVO getUserRedisSessionToken(Users user) {
        String uniqueToken = UUID.randomUUID().toString();
        redis.set(USER_REDIS_SESSION + ":" + user.getId(), uniqueToken, 1000 * 60 * 30);
        UsersVO userVO = DozerUtils.convert(user, UsersVO.class);
        userVO.setUserToken(uniqueToken);
        return userVO;
    }
}
