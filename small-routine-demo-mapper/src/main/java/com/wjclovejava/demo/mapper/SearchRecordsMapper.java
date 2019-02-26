package com.wjclovejava.demo.mapper;


import com.wjclovejava.demo.common.utils.MyMapper;
import com.wjclovejava.demo.pojo.SearchRecords;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {

    List<String> getHotWords();
}