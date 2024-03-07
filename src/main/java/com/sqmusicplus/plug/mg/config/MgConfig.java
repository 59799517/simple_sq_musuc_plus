package com.sqmusicplus.plug.mg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname MgConfig
 * @Description 咪咕配置文件
 * @Version 1.0.0
 * @Date 2023/3/24 17:05
 * @Created by shang
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mg")
public class MgConfig {
    private String SearchUrl;

    private String SongInfoUrl;

    private String ArtistInfoUrl;

    private String AlbumInfoUrl;

    private String ArtistAlbumListUrl;

    private String  AlbumIdConvert;

    private String AlbumListUrl;

    private String SongCoverUrl;

    private String DownloadUrl;

    private String DownloadUrlPrefix;

    private String ArtistSongListUrl;

    private String SongLyricUrl;
}
