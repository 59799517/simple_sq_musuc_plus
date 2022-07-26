package com.sqmusicplus.plug.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.sqmusicplus.entity.Music;
import com.sqmusicplus.plug.kw.hander.KWSearchHander;
import com.sqmusicplus.utils.DownloadUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/6/12
 * Time: 2:12
 * Description: 下载池
 */
@Data
@Slf4j
//@Component
public class DownloadPool {

//    //下载对象
//    public  List<DownloadMusicDTO> downloadPool = new ArrayList<>();
    //重试次数
    public  int retriesNum = 3;
    public int downloadCount =0;
    //需要重试的对象
    public  List<DownloadMusicDTO> retriesPool= new ArrayList<>();
    //重试后仍然错误的对象
    public  List<DownloadMusicDTO> errorPool= new ArrayList<>();

    public  HashMap<String,Integer> record  = new HashMap<>();

    public KWSearchHander searchHander = SpringUtil.getBean(KWSearchHander.class);




    @Data
    public class DownloadMusicDTO{
        Music music;
        String downloadUrl;
        File type;
    }


    public void add(){
        downloadCount++;
    }

    /**
     * 添加到重试中
     */
    public void  addToRetriesPool(String url, Music music, File type){
        DownloadMusicDTO downloadMusicDTO = new DownloadMusicDTO();
        downloadMusicDTO.setMusic(music);
        downloadMusicDTO.setDownloadUrl(url);
        downloadMusicDTO.setType(type);
       //重试次数
       Integer integer = record.get(url);
       if (integer==null){
           //第一次进来
           retriesPool.add(downloadMusicDTO);
           record.put(downloadMusicDTO.getDownloadUrl(),1);
           excute(downloadMusicDTO);

       }else{
           //查看第几次 超过次数就踢出存入错误库
           if ( integer.byteValue()>retriesNum){
               retriesPool.remove(downloadMusicDTO);
               errorPool.add(downloadMusicDTO);
           }else{
               excute(downloadMusicDTO);
               int i = integer.intValue() + 1;
               record.put(downloadMusicDTO.getDownloadUrl(),i);
           }
       }
    }

    void  excute(DownloadMusicDTO downloadMusicDTO){
        DownloadUtils.download(downloadMusicDTO.getDownloadUrl(),downloadMusicDTO.getType(),onSuccess ->{
            searchHander.savetodb(downloadMusicDTO.getType(),downloadMusicDTO.getMusic());
            retriesPool.remove(downloadMusicDTO);
            record.remove(downloadMusicDTO.getDownloadUrl());
        },onFailure ->{
        });
    }
}
