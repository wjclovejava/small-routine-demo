package com.wjclovejava.demo.service.impl;

import com.wjclovejava.demo.mapper.UsersFansMapper;
import com.wjclovejava.demo.mapper.UsersLikeVideosMapper;
import com.wjclovejava.demo.mapper.UsersMapper;
import com.wjclovejava.demo.mapper.UsersReportMapper;
import com.wjclovejava.demo.pojo.Users;
import com.wjclovejava.demo.pojo.UsersFans;
import com.wjclovejava.demo.pojo.UsersLikeVideos;
import com.wjclovejava.demo.pojo.UsersReport;
import com.wjclovejava.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @Author: wjc
 * @Description:
 * @Date: created in 2019/2/21 11:54
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;
    @Autowired
    private UsersFansMapper usersFansMapper;
    @Autowired
    private UsersReportMapper usersReportMapper;
    @Autowired
    private Sid sid;

    @Override
    public boolean queryUsernameIsExist(String username) {

        Users user = new Users();
        user.setUsername(username);
        Users users = usersMapper.selectOne(user);
        return users == null ? false : true;
    }

    @Override
    public void saveUser(Users user) {
        String userId=sid.nextShort();
        user.setId(userId);
        usersMapper.insert(user);
    }

    @Override
    public Users queryUserForLogin(String username, String password) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", password);
        Users result = usersMapper.selectOneByExample(userExample);
        return result;
    }

    @Override
    public void updateUserInfo(Users user) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id",user.getId());
        usersMapper.updateByExampleSelective(user,userExample);
    }


    @Override
    public Users queryUserInfo(String userId) {

        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id",userId);
        Users user = usersMapper.selectOneByExample(userExample);
        return user;
    }

    @Override
    public boolean isUserLikeVideo(String loginUserId, String videoId) {
        if (StringUtils.isBlank(loginUserId) || StringUtils.isBlank(videoId)) {
            return false;
        }
        Example example = new Example(UsersLikeVideos.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", loginUserId);
        criteria.andEqualTo("videoId", videoId);
        List<UsersLikeVideos> list = usersLikeVideosMapper.selectByExample(example);

        if (list != null && list.size() >0) {
            return true;
        }
        return false;
    }

    @Override
    public void saveUserFanRelation(String userId, String fanId) {
        String relId = sid.nextShort();

        UsersFans userFan = new UsersFans();
        userFan.setId(relId);
        userFan.setUserId(userId);
        userFan.setFanId(fanId);

        usersFansMapper.insert(userFan);

        usersMapper.addFansCount(userId);
        usersMapper.addFollersCount(fanId);
    }

    @Override
    public void deleteUserFanRelation(String userId, String fanId) {
        Example example = new Example(UsersFans.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("fanId", fanId);

        usersFansMapper.deleteByExample(example);

        usersMapper.reduceFansCount(userId);
        usersMapper.reduceFollersCount(fanId);
    }

    @Override
    public boolean queryIfFollow(String userId, String fanId) {
        Example example = new Example(UsersFans.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("fanId", fanId);

        List<UsersFans> list = usersFansMapper.selectByExample(example);

        if (list != null && !list.isEmpty() && list.size() > 0) {
            return true;
        }

        return false;
    }

    @Override
    public void reportUser(UsersReport userReport) {
        String urId = sid.nextShort();
        userReport.setId(urId);

        usersReportMapper.insert(userReport);
    }
}
