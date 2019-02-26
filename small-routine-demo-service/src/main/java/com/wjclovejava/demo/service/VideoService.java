package com.wjclovejava.demo.service;

import com.wjclovejava.demo.common.utils.PagedResult;
import com.wjclovejava.demo.pojo.Comments;
import com.wjclovejava.demo.pojo.Videos;

import java.util.List;

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

    /**
     * 分页查询视频
     * @return
     */
    PagedResult getAllVideos(Videos videos,Integer isSaveRecord,Integer page,Integer pageSize);

    /**
     * 获取热搜词列表
     * @return
     */
    List<String> getHotwords();
    /**
     * 保存留言
     * @return
     */
    void saveComments(Comments comment);
    /**
     * 分页查询留言板
     * @return
     */
    PagedResult getAllComments(String videoId, Integer page, Integer pageSize);
}
