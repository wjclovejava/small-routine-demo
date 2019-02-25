package com.wjclovejava.demo.service.impl;

import com.wjclovejava.demo.mapper.VideosMapper;
import com.wjclovejava.demo.pojo.Users;
import com.wjclovejava.demo.pojo.Videos;
import com.wjclovejava.demo.service.VideoService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author: wjc
 * @Description:
 * @Date: created in 2019/2/25 10:54
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideosMapper videosMapper;
    @Autowired
    private Sid sid;
    @Override
    public String saveVideo(Videos video) {
        String id = sid.nextShort();
        video.setId(id);
        videosMapper.insertSelective(video);
        return id;
    }

    @Override
    public void updateVideo(String videoId, String coverPath) {
        Videos videos=new Videos();
        videos.setCoverPath(coverPath);
        Example userExample = new Example(Videos.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id",videoId);
        videosMapper.updateByExampleSelective(videos,userExample);
    }
}
