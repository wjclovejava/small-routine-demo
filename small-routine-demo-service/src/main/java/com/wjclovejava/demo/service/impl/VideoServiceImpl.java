package com.wjclovejava.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wjclovejava.demo.common.utils.PagedResult;
import com.wjclovejava.demo.common.utils.TimeAgoUtils;
import com.wjclovejava.demo.mapper.*;
import com.wjclovejava.demo.pojo.Comments;
import com.wjclovejava.demo.pojo.SearchRecords;
import com.wjclovejava.demo.pojo.Videos;
import com.wjclovejava.demo.pojo.vo.CommentsVO;
import com.wjclovejava.demo.pojo.vo.VideosVO;
import com.wjclovejava.demo.service.VideoService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

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
    private VideosMapperCustom videosMapperCustom;

    @Autowired
    private SearchRecordsMapper searchRecordsMapper;

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private CommentsMapperCustom commentsMapperCustom;

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

    @Override
    public PagedResult getAllVideos(Videos videos,Integer isSaveRecord,Integer page, Integer pageSize) {

        String desc=videos.getVideoDesc();
        //保存 热搜词
        if(1 == isSaveRecord){
            SearchRecords record=new SearchRecords();
            record.setId(sid.nextShort());
            record.setContent(desc);
            searchRecordsMapper.insert(record);
        }

        PageHelper.startPage(page, pageSize);
        List<VideosVO> list = videosMapperCustom.queryAllVideos(desc);
        PageInfo<VideosVO> pageList =new PageInfo<>(list);
        PagedResult pagedResult=new PagedResult();
        //当前页
        pagedResult.setPage(page);
        //总页数
        pagedResult.setTotal(pageList.getPages());
        //数据
        pagedResult.setRows(list);
        //总记录数
        pagedResult.setRecords(pageList.getTotal());
        return pagedResult;
    }

    @Override
    public List<String> getHotwords() {
        return searchRecordsMapper.getHotWords();
    }

    @Override
    public void saveComments(Comments comment) {
        String id = sid.nextShort();
        comment.setId(id);
        commentsMapper.insert(comment);
    }

    @Override
    public PagedResult getAllComments(String videoId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<CommentsVO> list = commentsMapperCustom.queryComments(videoId);

        for (CommentsVO c : list) {
            String timeAgo = TimeAgoUtils.format(c.getCreateTime());
            c.setTimeAgoStr(timeAgo);
        }

        PageInfo<CommentsVO> pageList = new PageInfo<>(list);

        PagedResult grid = new PagedResult();
        grid.setTotal(pageList.getPages());
        grid.setRows(list);
        grid.setPage(page);
        grid.setRecords(pageList.getTotal());

        return grid;
    }
}
