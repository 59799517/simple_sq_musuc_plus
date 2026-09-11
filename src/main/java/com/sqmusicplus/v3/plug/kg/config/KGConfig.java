package com.sqmusicplus.v3.plug.kg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname KGConfig
 * @Description 酷狗配置
 * @Version 1.0.0
 * @Date 2025/2/5 15:32
 * @Created by SQ
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "kg")
public class KGConfig {

    private  String refreshTokenUrl ;
    private  String searchUrl ;
    private  String qrCodeKeyUrl ;
    private  String qrCodeUrl ;
    private  String qrCodeCheckUrl ;
    private  String wxQrCodeUrl ;
    private  String wxQrCodeCheckUrl ;
    private  String wxQropenplatUrl ;
    private  String signUrl ;
    private  String signInfoUrl;
    private  String vipUpgrade ;
    private  String songinfoUrl ;
    private  String songinfoAddUrl;
    private  String lyricIdUrl;
    private  String lyricUrl;
    private  String singerAlbumUrl;
    private  String singerInfoUrl;
    private  String imageSize;
    private  String albumInfoUrl;
    private  String albumSongUrl;
    private  String downloadUrl;
    private  String downloadUrl2;
    private  String userPlaylist;
    private  String SearchTipUrl;
}
