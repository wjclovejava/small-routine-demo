package com.wjclovejava.demo.mini.api.controller;

import com.wjclovejava.demo.common.utils.BasicResult;
import com.wjclovejava.demo.common.utils.DozerUtils;
import com.wjclovejava.demo.mini.api.controller.vo.UsersVO;
import com.wjclovejava.demo.pojo.Users;
import com.wjclovejava.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @Author: wjc
 * @Description:
 * @Date: created in 2019/2/22 10:57
 */
@RestController
@RequestMapping("/user")
@Api(value = "用户控制器", tags = "用户相关业务的Controller")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("用户上传头像")
    @PostMapping("/uploadFace")
    public BasicResult uploadFace(String userId, @RequestParam("file") MultipartFile[] files){
        //文件保存的命名空间
        String FileSpace = "F:/video_data";
        //保存到数据库中的相对路径
        String uploadPathDB = "/" + userId + "/face";
        if(StringUtils.isBlank(userId)){
            return BasicResult.errorMsg("用户id不可为空");
        }
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {

            if (files != null && files.length > 0) {

                String filename = files[0].getOriginalFilename();
                if(StringUtils.isNotBlank(filename)){
                    //文件上传最终保存路径
                    String finalFacePath=FileSpace+uploadPathDB+"/"+filename;
                    //设置数据库保存路径
                    uploadPathDB+=("/"+filename);

                    File outFile =new File(finalFacePath);

                    if(outFile.getParentFile() !=null || !outFile.getParentFile().isDirectory()){
                        //创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    fileOutputStream=new FileOutputStream(outFile);
                    inputStream=files[0].getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return BasicResult.errorMsg("上传出错");
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
        //修改用户头像地址
        Users user =new Users();
        user.setId(userId);
        user.setFaceImage(uploadPathDB);
        userService.updateUserInfo(user);

        return BasicResult.ok(uploadPathDB);
    }



    @ApiOperation("查询用户信息")
    @PostMapping("/query")
    public BasicResult queryUser(String userId){
        if(StringUtils.isBlank(userId)){
            return BasicResult.errorMsg("用户id不能为空");
        }
        Users user = userService.queryUserInfo(userId);
        UsersVO usersVO=DozerUtils.convert(user, UsersVO.class);
        return BasicResult.ok(usersVO);
    }


}
