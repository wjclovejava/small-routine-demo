package com.wjclovejava.demo.mini.api.controller;

import com.wjclovejava.demo.common.utils.BasicResult;
import com.wjclovejava.demo.service.BgmService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wjc
 * @Description:
 * @Date: created in 2019/2/23 10:46
 */
@RestController
@RequestMapping("/bgm")
@Api(value = "背景音乐控制器", tags = "背景音乐业务的Controller")
public class BgmController {
    @Autowired
    private BgmService bgmService;

    @PostMapping("/list")
    public BasicResult list(){
        return BasicResult.ok(bgmService.queryBgmList());
    }
}
