package com.sqmusicplus.plug.kw.config;

import com.sqmusicplus.plug.base.config.PlugConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname KwConfig
 * @Description 酷我配置文件
 * @Version 1.0.0
 * @Date 2022/5/19 9:57
 * @Created by SQ
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "kw")
public class KwConfig extends PlugConfig {
    private String SearchUrl;
    private String BangMenuUrl;
    private String BangInfoUrl;
    private String downloadurl;
    private String SongInfoUrl;
    private String SongCoverUrl;
    private String Searheads;
    private String ArtistInfoUrl;
    private String AlbumListUrl;
    private String AlbumInfoUrl;
    private String ArtistAlbumListUrl;
    private String ArtistSongListUrl;
    private String PlayListInfo;


    @Override
    public String getStringPlugSetName() {
        return getId() + ":" + getName();
    }
}
