package com.wjclovejava.demo.mapper;

import java.util.List;

import com.wjclovejava.demo.common.utils.MyMapper;
import com.wjclovejava.demo.pojo.Comments;
import com.wjclovejava.demo.pojo.vo.CommentsVO;

public interface CommentsMapperCustom extends MyMapper<Comments> {
	
	List<CommentsVO> queryComments(String videoId);
}