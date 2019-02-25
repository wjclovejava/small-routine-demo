package com.wjclovejava.demo.service.impl;

import com.wjclovejava.demo.mapper.UsersMapper;
import com.wjclovejava.demo.pojo.Users;
import com.wjclovejava.demo.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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
}
