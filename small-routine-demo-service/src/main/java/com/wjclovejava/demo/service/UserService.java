package com.wjclovejava.demo.service;

import com.wjclovejava.demo.pojo.Users;

/**
 * @Author: wjc
 * @Description:
 * @Date: created in 2019/2/21 11:52
 */
public interface UserService {
    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
     boolean queryUsernameIsExist(String username);

    /**
     * 保存用户
     * @param user
     */
    void saveUser(Users user);

    /**
     * 用户登录
     * @param username
     * @param password
     */
    Users queryUserForLogin( String username,String password);

    /**
     * 修改用户信息
     * @param user
     */
    void updateUserInfo( Users user);

    /**
     *
     * @param userId
     * @return
     */
    Users queryUserInfo(String userId);
}
