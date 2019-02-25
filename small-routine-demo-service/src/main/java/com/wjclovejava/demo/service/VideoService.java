package com.wjclovejava.demo.service;

import com.wjclovejava.demo.pojo.Videos;

/**
 * @Author: wjc
 * @Description:
 * @Date: created in 2019/2/25 10:54
 */
public interface VideoService {

    /**
     *保存视频,返回主键id
     * @param videos
     * @return
     */
    String saveVideo(Videos videos);

    /**
     * 修改视频封面
     * @param videoId
     * @return
     */
    void updateVideo(String videoId,String coverPath);
}
