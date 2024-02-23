package com.sqmusicplus.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ejlchina.data.Mapper;
import com.ejlchina.okhttps.HTTP;
import com.sqmusicplus.base.entity.SqConfig;
import com.sqmusicplus.download.DownloadExcute;
import com.sqmusicplus.plug.netease.config.NeteaseConfig;
import com.sqmusicplus.plug.netease.hander.NeteaseHander;
import com.sqmusicplus.plug.subsonic.SubsonicHander;
import com.sqmusicplus.plug.subsonic.config.NowPlayList;
import com.sqmusicplus.plug.subsonic.entity.SubsonicPlayList;
import com.sqmusicplus.base.service.SqConfigService;
import com.sqmusicplus.utils.DownloadUtils;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import top.yumbo.util.music.MusicEnum;

import java.util.ArrayList;

/**
 * @Classname Init
 * @Description 初始化
 * @Version 1.0.0
 * @Date 2022/8/3 16:52
 * @Created by SQ
 */
@Slf4j
@Configuration
public class Init implements ApplicationRunner {
    @Autowired
    private SqConfigService configService;
    @Autowired
    private SubsonicHander subsonicHander;
    @Value("${server.port}")
    private String port;
    @Autowired
    private DownloadExcute downloadExcute;
    @Autowired
    private NeteaseHander neteaseHander;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        SqConfig init_download = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.init.download"));
        SqConfig subsonic = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.start"));
        if (Boolean.getBoolean(subsonic.getConfigValue())) {
            Boolean aBoolean = subsonicHander.checkLoginInfo();
            if (aBoolean) {
                log.info("subsonic服务连接正常");
            } else {
                configService.update(new UpdateWrapper<SqConfig>().eq("config_key", "plug.subsonic.start").set("config_key", "false"));
            }
            ArrayList<SubsonicPlayList> subsonicPlayList = subsonicHander.getSubsonicPlayList();
            for (SubsonicPlayList playList : subsonicPlayList) {
                NowPlayList.NOW_PLAYLIST.put(playList.getName(), playList);
                log.info("subsonic加载歌单-{}", playList.getName());
            }
        }
        log.info("启动完毕：http://localhost:{}", port);
        if (Boolean.valueOf(init_download.getConfigValue())){
            downloadExcute.getDownloadInfo();
        }
        neteaseHander.initPlug();



    }


}
