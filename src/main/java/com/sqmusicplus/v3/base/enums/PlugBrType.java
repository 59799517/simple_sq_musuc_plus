package com.sqmusicplus.v3.base.enums;


import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public enum PlugBrType {

    KW_MP3_128( "128kmp3", "mp3", 128,"kw","nKwSearchHander","kw_mp3_128"),
    KW_MP3_192( "192kmp3", "mp3", 192,"kw","nKwSearchHander","kw_mp3_192"),
    KW_MP3_320( "320kmp3", "mp3", 320,"kw","nKwSearchHander","kw_mp3_320"),
    KW_APE_1000( "1000kape", "ape", 1000,"kw","nKwSearchHander","kw_ape_1000"),
    KW_FLAC_2000( "2000kflac", "flac", 2000,"kw","nKwSearchHander","kw_flac_2000"),
//    MG_FLAC_2000("ZQ","flac",2000,"mg","mgHander","mg_flac_2000"),
//    MG_FLAC_1000("SQ","flac",1000,"mg","mgHander","mg_flac_1000"),
    MG_MP3_320("HQ","mp3",320,"mg","mgHander","mg_mp3_320"),
    MG_MP3_128("PQ","mp3",128,"mg","mgHander","mg_mp3_128"),
    MG_MP3_64("LQ","mp3",64,"mg","mgHander","mg_mp3_64"),
//
//    QQ_MP3_128("HQ_M500","mp3",128,"qq","qqHander","qq_mp3_128"),
//    QQ_MP3_320("HQ_M800","mp3",320,"qq","qqHander","qq_mp3_320"),
//    QQ_Flac_2000("SQ_F000","flac",2000,"qq","qqHander","qq_flac_2000"),
//    QQ_Flac_3000("HR_RS01","flac",3000,"qq","qqHander","qq_flac_3000"),
//    QQ_Flac_4000("HR_Q000","flac",3000,"qq","qqHander","qq_flac_4000"),
//    QQ_Flac_5000("HR_AI00","flac",3000,"qq","qqHander","qq_flac_5000"),

    NETEASE_MP3_128("standard", "mp3", 128,"netease","neteaseHander","netease_mp3_128"),
    NETEASE_MP3_192("higher", "mp3", 192,"netease","neteaseHander","netease_mp3_192"),
    NETEASE_MP3_320("exhigh", "mp3", 320,"netease","neteaseHander","netease_mp3_320"),
    NETEASE_FLAC_2000("lossless", "flac", 2000,"netease","neteaseHander","netease_flac_2000"),
    NETEASE_FLAC_3000("hires", "flac", 3000,"netease","neteaseHander","netease_flac_3000"),



    QQVIP_MP3_128("128","mp3",128,"qqvip","qqvipHander","qqvip_mp3_128"),
    QQVIP_MP3_320("320","mp3",320,"qqvip","qqvipHander","qqvip_mp3_320"),
    QQVIP_Flac_2000("flac","flac",2000,"qqvip","qqvipHander","qqvip_flac_2000"),
    QQVIP_Flac_3000("flac","flac",3000,"qqvip","qqvipHander","qqvip_flac_3000"),
    QQVIP_Flac_4000("flac","flac",4000,"qqvip","qqvipHander","qqvip_flac_4000"),
    QQVIP_Flac_5000("flac","flac",5000,"qqvip","qqvipHander","qqvip_flac_5000"),
    QQVIP_Ape_2000("ape","ape",2000,"qqvip","qqvipHander","qqvip_ape_2000"),
    QQVIP_M4A_2000("m4a","m4a",3000,"qqvip","qqvipHander","qqvip_m4a_2000"),


    Free_Download_2000("flac","flac",2000,"free","freeMp3Hander","free_flac_2000"),

    KG_MP3_128("128", "mp3", 128,"kg","kgHander","kg_mp3_128"),
//    KG_MP3_320("320", "mp3", 320,"kg","kgHander","kg_mp3_320"),
//    KG_Flac_890("flac", "flac", 890,"kg","kgHander","kg_flac_890"),
//    KG_Flac_2000("flac", "flac", 2000,"kg","kgHander","kg_flac_2000"),
//    KG_Flac_3000("high", "flac", 3000,"kg","kgHander","kg_flac_3000"),
//    KG_Flac_4000("viper_atmos", "flac", 4000,"kg","kgHander","kg_flac_4000"),
//    KG_Flac_5000("viper_tape", "flac", 5000,"kg","kgHander","kg_flac_5000"),


//    APPLE_MP3_320("320","mp3",320,"apple","appleHander","apple_mp3_320"),
//    APPLE_AAC_256("3000","aac",256,"apple","appleHander","apple_aac_256"),
//    APPLE_OGG_257("256","ogg",257,"apple","appleHander","apple_ogg_257"),
//    APPLE_M4A_258("257","m4a",258,"apple","appleHander","apple_m4a_258"),
//    APPLE_WAV_1500("1500","wav",1500,"apple","appleHander","apple_wav_1500"),
//    APPLE_FLAC_2000("2000","flac",2000,"apple","appleHander","apple_flac_2000"),
//    APPLE_SOURCE_9999("2000","m4a",9999,"apple","appleHander","apple_source_9999"),

    TIDAL_M4A_320("HIGH","mp3",320,"tidal","tidalSearchHander","tidal_m4a_320"),
    TIDAL_FLAC_LOSSLESS("LOSSLESS","flac",1411,"tidal","tidalSearchHander","tidal_flac_lossless"),
    TIDAL_HI_FLAC_RES_LOSSLESS("HI_RES_LOSSLESS","flac",2800,"tidal","tidalSearchHander","tidal_flac_res_lossless"),

    QOBUZ_MP3_320("320","mp3",320,"qobuz","qobuzSearchHander","qobuz_mp3_320"),
    QOBUZ_FLAC_LOSSLESS("lossless","flac",1411,"qobuz","qobuzSearchHander","qobuz_flac_lossless"),
    QOBUZ_FLAC_HIRES("hires","flac",2800,"qobuz","qobuzSearchHander","qobuz_flac_hires");

    //汽水音乐（Qishui / 抖音音乐 LunaPC）
    //档位对齐汽水 track.bit_rates[].quality 实际返回的五种音质：
    //  medium(标准 约66kbps) / higher(较高 约132kbps) / highest(极高 约260kbps)
    //  lossless(无损 约806kbps) / spatial(空间音频，暂不单独暴露)
    //标准 medium
//    QISHUI_M4A_128("medium","m4a",128,"qishui","qishuiHander","qishui_m4a_128"),
//    //较高 higher
//    QISHUI_M4A_192("higher","m4a",192,"qishui","qishuiHander","qishui_m4a_192"),
//    //极高 highest
//    QISHUI_M4A_320("highest","m4a",320,"qishui","qishuiHander","qishui_m4a_320"),
//    //无损 lossless（需登录VIP）
//    QISHUI_FLAC_2000("lossless","flac",2000,"qishui","qishuiHander","qishui_flac_2000");
//






    String value;
    String type;
    Integer bit;

    String plugName;

    String springName;
    String id;


    PlugBrType(String value, String type, Integer bit, String plugName, String springName, String id) {
        this.value = value;
        this.type = type;
        this.bit = bit;
        this.plugName = plugName;
        this.springName = springName;
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public Integer getBit() {
        return bit;
    }

    public String getPlugName() {
        return plugName;
    }

    public String getSpringName() {
        return springName;
    }

    public String getId() {
        return id;
    }
    //根据id找出枚举
    public static PlugBrType findById(String id) {
        for (PlugBrType value : PlugBrType.values()) {
            if (value.getId().equals(id)) {
                return value;
            }
        }
        return null;
    }
    //根据plugName和bit找出枚举
    public static PlugBrType findByPlugNameAndBit(String plugName, Integer bit) {
        for (PlugBrType value : PlugBrType.values()) {
            if (value.getPlugName().equals(plugName) && value.getBit().equals(bit)) {
                return value;
            }
        }
        return null;
    }


//    public static List<PlugBrType> getAppleAllType(){
//        return List.of(APPLE_MP3_320,APPLE_AAC_256,APPLE_OGG_257,APPLE_M4A_258,APPLE_WAV_1500,APPLE_FLAC_2000,APPLE_SOURCE_9999);
//    }

    /**
     * 根据 type（文件格式如 flac/mp3）和 plugName 找出该类型中 bit 最大的枚举
     * 当 type 为 "auto" 时，不区分格式，取该 plugName 下所有类型中 bit 最大的枚举
     * @param type 文件格式，例如 "flac"、"mp3"、"ape"、"m4a"、"wav"，或 "auto" 表示不区分类型
     * @param plugName 插件名称，例如 "kw"、"qq"、"netease"
     * @return bit 最大的 PlugBrType，如果没有匹配的则返回 null
     */
    public static PlugBrType findMaxByTypeAndPlugName(String type, String plugName) {
        PlugBrType result = null;
        boolean isAuto = "auto".equals(type);
        for (PlugBrType value : PlugBrType.values()) {
            if (value.getPlugName().equals(plugName) && (isAuto || value.getType().equals(type))) {
                if (result == null || value.getBit() > result.getBit()) {
                    result = value;
                }
            }
        }
        return result;
    }


    /**
     * 根据 type（文件格式如 flac/mp3）和 plugName 找出该类型中 bit 最大的枚举
     * @param type
     * @param plugName
     * @param bits
     * @return
     */
    public static PlugBrType findMaxByTypeAndPlugNameAndBits(String type, String plugName, List<PlugBrType> bits) {
        PlugBrType result = null;
        boolean isAuto = "auto".equals(type);
        if (isAuto){
            //如果是自动则获取所有类型中 bits 方法中的所有枚举 最大的枚举
            for (PlugBrType bit : bits) {
                if (bit.getPlugName().equals(plugName)) {
                    if (result == null || bit.getBit() > result.getBit()) {
                        result = bit;
                    }
                }
            }
        }

        else{
            //否则获取 type 类型中 bits 方法中的所有枚举 最大的枚举
            for (PlugBrType bit : bits) {
                if (bit.getPlugName().equals(plugName) && bit.getType().equals(type)) {
                    if (result == null || bit.getBit() > result.getBit()) {
                        result = bit;
                    }
                }
            }
        }
        return result;
    }

}
