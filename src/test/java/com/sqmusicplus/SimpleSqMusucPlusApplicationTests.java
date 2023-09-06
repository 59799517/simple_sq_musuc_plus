package com.sqmusicplus;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.ejlchina.data.Mapper;
import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.OkHttps;

import com.ejlchina.okhttps.SHttpTask;
import com.sqmusicplus.plug.qq.config.QQConfig;
import com.sqmusicplus.plug.qq.entity.QQSearchEntity;
import com.sqmusicplus.plug.qq.enums.QQSearchType;
import com.sqmusicplus.utils.DateUtils;
import com.sqmusicplus.utils.DownloadUtils;
import com.sqmusicplus.utils.ZLibUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static cn.hutool.crypto.digest.DigestUtil.md5;

@SpringBootTest
class SimpleSqMusucPlusApplicationTests {
    @Autowired
    private QQConfig qqConfig;

//    @Test
//    void contextLoads()  {
//
//        String artistInfoUrl = qqConfig.getArtistInfoUrl();
//        String artistInfoReferer = qqConfig.getArtistInfoReferer();
//        String s = artistInfoUrl.replaceAll("#\\{mid}", "0025NhlN2yWrP4");
//        Mapper mapper1 = DownloadUtils.getHttp().sync(s).bodyType(OkHttps.XML).addHeader("Referer", artistInfoReferer).get().getBody().toMapper();
//        System.out.println(mapper1);
//
//    }
//    @Test
//    public void downloadUrl(){
//        String downloadurl = "http://nmobi.kuwo.cn/mobi.s?f=kuwo&q=";
//        String s = "user=e3cc098fd4c59ce2&android_id=e3cc098fd4c59ce2&prod=kwplayer_ar_9.3.1.3&corp=kuwo&newver=2&vipver=9.3.1.3&source=kwplayer_ar_9.3.1.3_qq.apk&p2p=1&notrace=0&type=convert_url2&br=#{brvalue}&format=flac|mp3|aac&sig=0&rid=#{musicId}&priority=bitrate&loginUid=435947810&network=WIFI&loginSid=1694167478&mode=download&uid=658048466";
//        try {
//            s = s.replaceAll("#\\{musicId}","184274130").replaceAll("#\\{brvalue}","2000");
//            byte[] bytes = KuwoDES.encrypt2(s.getBytes("UTF-8"), s.length(), KuwoDES.SECRET_KEY, KuwoDES.SECRET_KEY_LENG);
//            char[] encode = Base64Coder.encode(bytes);
//            String out =  new String(encode);
//            downloadurl =  downloadurl+out;
//        } catch (UnsupportedEncodingException e) {
////            log.error("获取下载链接失败：{}",e.getMessage());
//            return ;
//        }
//        String s1 = DownloadUtils.getHttp().sync(downloadurl).get().getBody().toByteString().utf8();
//        System.out.println(s1);
////        downloadurl= s1.split("\n")[2].split("=")[1].split("\r")[0];
//    }



    @Test
    public void contextLoads() throws IOException {
        String t1_MusicID = "001Zi7Ly4ZtVQk";
        String platform = "qq";
        String t2 = "SQ";
        String device = "MI 14 Pro Max";
        String osVersion = "13" ;
         String time = DateUtils.getNowDate().getTime()/1000+"";
        String  lowerCase = DigestUtil.md5Hex("6d849adb2f3e00d413fe48efbb18d9bb" + time + "6562653262383463363633646364306534333668");
        String   s6 = "{\\\"method\\\":\\\"GetMusicUrl\\\",\\\"platform\\\":\\\"" + platform + "\\\",\\\"t1\\\":\\\"" + t1_MusicID + "\\\",\\\"t2\\\":\\\"" + t2 + "\\\"}";
        String s7 = "{\\\"uid\\\":\\\"\\\",\\\"token\\\":\\\"\\\",\\\"deviceid\\\":\\\"84ac82836212e869dbeea73f09ebe52b\\\",\\\"appVersion\\\":\\\"4.1.2\\\",\\\"vercode\\\":\\\"4120\\\",\\\"device\\\":\\\"" + device + "\\\",\\\"osVersion\\\":\\\"" + osVersion + "\\\"}";
        String  s8 = "{\n\t\"text_1\":\t\"" + s6 + "\",\n\t\"text_2\":\t\"" + s7 + "\",\n\t\"sign_1\":\t\"" + lowerCase + "\",\n\t\"time\":\t\"" + time + "\",\n\t\"sign_2\":\t\"" + DigestUtil.md5Hex(
                s6.replace("\\", "") + s7.replace("\\", "") + lowerCase + time + "NDRjZGIzNzliNzEe") + "\"\n}" ;
        byte[] utf8Bytes = s8.getBytes(StandardCharsets.UTF_8);
        String hexString = ByteArrayUtil.toHexString(utf8Bytes);
        String upperHexString = hexString.toUpperCase();
        byte[] encodedBytes = upperHexString.getBytes(StandardCharsets.UTF_8);
        byte[] compress = ZLibUtils.compress(encodedBytes);
        HTTP http = DownloadUtils.getHttp();
        SHttpTask sync = http.sync("http://gcsp.kzti.top:1030/client/cgi-bin/api.fcg");
        sync.setBodyPara(compress);
        HttpResult post = sync.post();
        byte[] decompress = ZLibUtils.decompress(post.getBody().toBytes());
        String s = new String(decompress);
        System.out.println(s);
    }



}
