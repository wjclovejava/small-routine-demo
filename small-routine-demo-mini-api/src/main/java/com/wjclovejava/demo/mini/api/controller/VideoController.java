package com.wjclovejava.demo.mini.api.controller;

import com.wjclovejava.demo.common.enums.VideoStatusEnum;
import com.wjclovejava.demo.common.utils.BasicResult;
import com.wjclovejava.demo.common.utils.FFMpegUtils;
import com.wjclovejava.demo.common.utils.PagedResult;
import com.wjclovejava.demo.pojo.Bgm;
import com.wjclovejava.demo.pojo.Comments;
import com.wjclovejava.demo.pojo.Users;
import com.wjclovejava.demo.pojo.Videos;
import com.wjclovejava.demo.service.BgmService;
import com.wjclovejava.demo.service.VideoService;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: wjc
 * @Description:
 * @Date: created in 2019/2/23 10:46
 */
@RestController
@RequestMapping("/video")
@Api(value = "视频相关业务的接口", tags = "视频业务的Controller")
public class VideoController extends BasicController {
    @Value("${video.space}")
    private String fileSpace;
    @Autowired
    private BgmService bgmService;
    @Autowired
    private VideoService videoService;

    @ApiOperation("用户上传视频")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "form"),
                @ApiImplicitParam(name = "bgmId", value = "背景音乐id", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoSeconds", value = "视频秒数", required = true, dataType = "double", paramType = "form"),
            @ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "videoHeight", value = "视频宽度", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "desc", value = "视频描述", dataType = "String", paramType = "form")
        }
    )
    @PostMapping(value = "/uploadVideo",headers="content-type=multipart/form-data")
    public BasicResult uploadVideo(String userId,
                                   String bgmId,
                                   double videoSeconds,
                                   int videoWidth,
                                   int videoHeight,
                                   String desc,
                                   @ApiParam(value = "短视频",required = true)
                                   MultipartFile file){

        //保存到数据库中的相对路径
        String uploadPathDB = "/" + userId + "/video";
        //保存到数据库中的视频封面的相对路径
        String coverPathDB = "/" + userId + "/video";
        if(StringUtils.isBlank(userId)){
            return BasicResult.errorMsg("用户id不可为空");
        }
        FileOutputStream fileOutputStream = null;
        InputStream inputStream;
        String finalVideoPath="";
        try {

            if (file != null) {

                String filename = file.getOriginalFilename();
                String now = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
                String filenamePrefix= filename.split("\\.")[0]+now;
                if(StringUtils.isNotBlank(filename)){
                    //文件上传最终保存路径
                    finalVideoPath=fileSpace+uploadPathDB+"/"+filename;
                    //设置数据库保存路径
                    uploadPathDB+=("/"+filename);
                    coverPathDB =coverPathDB+"/"+filenamePrefix+".jpg";
                    File outFile =new File(finalVideoPath);

                    if(outFile.getParentFile() !=null || !outFile.getParentFile().isDirectory()){
                        //创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    fileOutputStream=new FileOutputStream(outFile);
                    inputStream=file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return BasicResult.errorMsg("上传视频出错");
        } finally {
            if(fileOutputStream !=null){
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //判断bgmId是否为空,若不为空.将视频与背景音乐合成新的视频
        if(StringUtils.isNotBlank(bgmId)){
            Bgm bgm=bgmService.queryBgmById(bgmId);
            String mp3InputPath =fileSpace + bgm.getPath();
            String videoInputPath=finalVideoPath;
            String videoOutputPathName=UUID.randomUUID().toString()+".mp4";
            uploadPathDB="/"+userId+"/video"+"/"+videoOutputPathName;
            String videoOutputPath=fileSpace+uploadPathDB;
            try {
                FFMpegUtils.convertor(videoInputPath, mp3InputPath, videoSeconds, videoOutputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("uploadPathDB:"+uploadPathDB);
        System.out.println("finalVideoPath:"+finalVideoPath);
        //对视频进行截图,保存视频第一秒第一帧图片
        try {
            FFMpegUtils.getCover(finalVideoPath, fileSpace+coverPathDB);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //保存视频信息到数据库
        Videos video=new Videos();
        video.setAudioId(bgmId);
        video.setUserId(userId);
        video.setVideoSeconds((float)videoSeconds);
        video.setVideoHeight(videoHeight);
        video.setVideoWidth(videoWidth);
        video.setCoverPath(coverPathDB);
        video.setStatus(VideoStatusEnum.SUCCESS.value);
        video.setVideoDesc(desc);
        video.setVideoPath(uploadPathDB);
        String videoId = videoService.saveVideo(video);
        return BasicResult.ok(videoId);
    }



    @ApiOperation("用户上传视频封面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoId", value = "视频主键id", required = true, dataType = "String", paramType = "form")
    }
    )
    @PostMapping(value = "/uploadVideoCover",headers="content-type=multipart/form-data")
    public BasicResult uploadVideoCover(String userId,
                                        String videoId,
                                   @ApiParam(value = "视频封面",required = true)
                                           MultipartFile file){

        //保存到数据库中的视频的相对路径
        String uploadPathDB = "/" + userId + "/video";
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(videoId)){
            return BasicResult.errorMsg("视频主键id/用户id不可为空");
        }
        FileOutputStream fileOutputStream = null;
        InputStream inputStream;
        try {

            if (file != null) {

                String filename = file.getOriginalFilename();
                if(StringUtils.isNotBlank(filename)){
                    //文件上传最终保存路径
                    String finalCoverPath=fileSpace+uploadPathDB+"/"+filename;
                    //设置数据库保存路径
                    uploadPathDB+=("/"+filename);

                    File outFile =new File(finalCoverPath);

                    if(outFile.getParentFile() !=null || !outFile.getParentFile().isDirectory()){
                        //创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    fileOutputStream=new FileOutputStream(outFile);
                    inputStream=file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return BasicResult.errorMsg("上传视频封面出错");
        } finally {
            if(fileOutputStream !=null){
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //更新视频封面 到数据库
        videoService.updateVideo(videoId, uploadPathDB);
        return BasicResult.ok();
    }

    /**
     *
     * @param videos
     * @param isSaveRecord 1 需要保存 0 不需要保存
     * @param page
     * @return
     */
    @ApiOperation("分页查询和搜索视频列表")
    @PostMapping("/showAll")
    public BasicResult showAll(@RequestBody Videos videos,Integer isSaveRecord,Integer page){
        if(null==page){
            page=1;
        }
        PagedResult result = videoService.getAllVideos(videos,isSaveRecord,page, PAGE_SIZE);
        return BasicResult.ok(result);
    }

    @ApiOperation("查询热搜词")
    @PostMapping("/hot")
    public BasicResult showHot(){
        return BasicResult.ok(videoService.getHotwords());
    }

    @ApiOperation("保存留言")
    @PostMapping("/saveComment")
    public BasicResult saveComment(@RequestBody Comments comments){
        videoService.saveComments(comments);
        return BasicResult.ok();
    }

    @ApiOperation("分页 获取视频的留言")
    @PostMapping("/getVideoComments")
    public BasicResult getVideoComments(String videoId, Integer page, Integer pageSize) throws Exception {

        if (StringUtils.isBlank(videoId)) {
            return BasicResult.ok();
        }
        // 分页查询视频列表，时间顺序倒序排序
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        PagedResult list = videoService.getAllComments(videoId, page, pageSize);

        return BasicResult.ok(list);
    }
}
