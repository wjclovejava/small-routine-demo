package com.wjclovejava.demo.service;


import com.wjclovejava.demo.pojo.Bgm;

import java.util.List;

/**
 * @Author: wjc
 * @Description:
 * @Date: created in 2019/2/23 10:39
 */
public interface BgmService{
    /**
     * 查询bgm背景音乐列表
     * @return
     */
    List<Bgm> queryBgmList();

    /**
     * 通过主键查询 bgm对象
     * @return
     */
    Bgm queryBgmById(String bgmId);
}
