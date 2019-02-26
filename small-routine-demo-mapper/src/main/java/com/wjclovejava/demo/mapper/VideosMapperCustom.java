package com.wjclovejava.demo.mapper;

import com.wjclovejava.demo.common.utils.MyMapper;
import com.wjclovejava.demo.pojo.Videos;
import com.wjclovejava.demo.pojo.vo.VideosVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosMapperCustom extends MyMapper<Videos> {

    List<VideosVO> queryAllVideos(@Param("videoDesc") String videoDesc);
}