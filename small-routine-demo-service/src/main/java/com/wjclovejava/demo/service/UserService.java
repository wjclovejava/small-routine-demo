package com.wjclovejava.demo.service;

import com.wjclovejava.demo.pojo.Users;
import com.wjclovejava.demo.pojo.UsersReport;

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

    /**
     * 查询用户是否喜欢的点赞视频
     * @param loginUserId
     * @param videoId
     * @return
     */
    boolean isUserLikeVideo(String loginUserId, String videoId);

    /**
     * @Description: 增加用户和粉丝的关系
     */
     void saveUserFanRelation(String userId, String fanId);

    /**
     * @Description: 删除用户和粉丝的关系
     */
     void deleteUserFanRelation(String userId, String fanId);

    /**
     * @Description: 查询用户是否关注
     */
     boolean queryIfFollow(String userId, String fanId);

    /**
     * @Description: 举报用户
     */
     void reportUser(UsersReport userReport);
}
