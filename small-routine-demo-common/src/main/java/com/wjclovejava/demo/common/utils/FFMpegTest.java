package com.wjclovejava.demo.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wjc
 * @Description:
 * @Date: created in 2019/2/25 10:05
 */
public class FFMpegTest {

    private String ffmpegEXE;

    public FFMpegTest(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public  void convertor(String videoInputPath,String videoOutputPath) throws IOException {
        //ffmpeg -i input.mp4 output.avi
        //java 调用cmd命令
        //命令通过空格分隔各个命令
        List<String> command =new ArrayList<>();
        command.add(ffmpegEXE);

        command.add("-i");
        command.add(videoInputPath);
        command.add(videoOutputPath);

        for(String c:command){
            System.out.print(c);
        }
        ProcessBuilder builder=new ProcessBuilder(command);
        Process process = builder.start();

        InputStream errorStream = process.getErrorStream();
        InputStreamReader isr=new InputStreamReader(errorStream);
        BufferedReader br=new BufferedReader(isr);

        String line ="";
        while((line =br.readLine()) !=null){
        }
        if(br !=null){
            br.close();
        }
        if(isr !=null){
            isr.close();
        }
        if(errorStream !=null){
            errorStream.close();
        }
    }


    public static void main(String[] args) {
        FFMpegTest ffMpegTest=new FFMpegTest("F:\\ffmpeg\\bin\\ffmpeg.exe");
        try {
            ffMpegTest.convertor("F:\\music.mp4", "F:\\music123.avi");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
