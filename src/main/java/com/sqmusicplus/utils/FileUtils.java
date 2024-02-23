package com.sqmusicplus.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;

import java.io.*;
import java.nio.file.Path;

/**
 * 文件处理工具类
 *
 * @author ruoyi
 */
public class FileUtils
{

    /**
     * 整理文件（移动）
     * @param file
     * @param target
     * @return
     */
    public static File  organizeFiles(File file,File target){
        Path move =target.toPath();
        String musicPath = file.getPath();
        //判断最后一个符号是文件分隔符
        if(!musicPath.endsWith(File.separator)){
            musicPath = musicPath + File.separator;
        }

        if(!file.exists()){
            file.mkdirs();
        }
        if(file.exists()){
            //移动文件
            move  = FileUtil.move(file.toPath(), target.toPath(), false);
        }
        return move.toFile();
    }

    public static File  organizeFiles(File file,String newFilepath,String newFileName){
        Assert.notNull(newFilepath, "路径不能为空");
        Assert.notNull(newFileName, "文件名称不能为空");
        //判断最后一个符号是文件分隔符
        if(!newFilepath.endsWith(File.separator)){
            newFilepath = newFilepath + File.separator;
        }
        File target = new File(newFilepath + newFileName);
        return organizeFiles(file,target);
    }

    public static File findFile(String path,String fileName){
        try {
            File file = new File(path);
            if(file.exists()){
                File[] files = file.listFiles();
                for(File f:files){
                    //判断文件的名称忽略后缀
                    String name = f.getName().substring(0,f.getName().lastIndexOf("."));
                    if(name.equals(fileName)){
                        return f;
                    }
                }
            }
        } catch (Exception e) {
            return new File("");
        }
        return new File("");
    }


}
