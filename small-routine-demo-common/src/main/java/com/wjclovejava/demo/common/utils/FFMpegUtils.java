package com.wjclovejava.demo.common.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wjc
 * @Description: 视频处理工具
 * @Date: created in 2019/2/25 10:05
 */
@Component
public class FFMpegUtils {

    //ffmpeg.exe 存放地址
    public static final String ffmpegEXE = "F:\\ffmpeg\\bin\\ffmpeg.exe";

    /**
     *  将视频与背景音乐合成新视频
     * @param videoInputPath 视频地址名称
     * @param mp3InputPath 音乐地址名称
     * @param seconds 视频播放秒数
     * @param videoOutputPath 合成视频存放地址名称
     * @throws IOException
     */
    public static void convertor(String videoInputPath,String mp3InputPath,double seconds,String videoOutputPath) throws IOException {
        //命令:ffmpeg -i input.mp4 -i input.mp3 -t 15 -y new.mp4
        //java 调用cmd命令
        //命令通过空格分隔各个命令
        List<String> command =new ArrayList<>();
        command.add(ffmpegEXE);
        command.add("-i");
        command.add(videoInputPath);
        command.add("-i");
        command.add(mp3InputPath);
        command.add("-t");
        command.add(String.valueOf(seconds));
        command.add("-y");
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

    public static void getCover(String videoInputPath, String coverOutputPath) throws IOException, InterruptedException {
//		ffmpeg.exe -ss 00:00:01 -i spring.mp4 -vframes 1 bb.jpg
        List<String> command = new java.util.ArrayList<String>();
        command.add(ffmpegEXE);

        // 指定截取第1秒
        command.add("-ss");
        command.add("00:00:01");

        command.add("-y");
        command.add("-i");
        command.add(videoInputPath);

        command.add("-vframes");
        command.add("1");

        command.add(coverOutputPath);

        for (String c : command) {
            System.out.print(c + " ");
        }

        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = builder.start();

        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader br = new BufferedReader(inputStreamReader);

        String line = "";
        while ( (line = br.readLine()) != null ) {
        }

        if (br != null) {
            br.close();
        }
        if (inputStreamReader != null) {
            inputStreamReader.close();
        }
        if (errorStream != null) {
            errorStream.close();
        }
    }
}
