package com.sqmusicplus.plug.qq.config;

import com.sqmusicplus.plug.base.config.PlugConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname QQConfig
 * @Description qq配置
 * @Version 1.0.0
 * @Date 2023/8/24 16:52
 * @Created by Administrator
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "qq")
public class QQConfig extends PlugConfig {

    //搜索
    private String SearchUrl;
    //专题图片
    private String albumImage;
    //歌手
    private String artistImage;

    //歌手详情
    private String ArtistInfoUrl;
    //歌手详情Referer
    private String ArtistInfoReferer;
    //下载接口
    private String downloadUrl;





    @Override
    public String getStringPlugSetName() {
        return "qq";
    }
}
