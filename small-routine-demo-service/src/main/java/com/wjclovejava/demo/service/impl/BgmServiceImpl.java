package com.wjclovejava.demo.service.impl;

import com.wjclovejava.demo.mapper.BgmMapper;
import com.wjclovejava.demo.mapper.UsersMapper;
import com.wjclovejava.demo.pojo.Bgm;
import com.wjclovejava.demo.pojo.Users;
import com.wjclovejava.demo.service.BgmService;
import com.wjclovejava.demo.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author: wjc
 * @Description:
 * @Date: created in 2019/2/23 10:44
 */
@Service
public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmMapper bgmMapper;

    @Override
    public List<Bgm> queryBgmList() {
        return bgmMapper.selectAll();
    }

    @Override
    public Bgm queryBgmById(String bgmId) {
        Bgm bgm=new Bgm();
        bgm.setId(bgmId);
        return bgmMapper.selectOne(bgm);
    }
}
