package com.sqmusicplus;

import com.sqmusicplus.plug.kw.enums.KwBrType;
import com.sqmusicplus.plug.utils.Base64Coder;
import com.sqmusicplus.plug.utils.KuwoDES;
import com.sqmusicplus.utils.DownloadUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//@SpringBootTest
class SimpleSqMusucPlusApplicationTests {

    @Test
    void contextLoads() throws EncoderException {
        MultimediaObject multimediaObject = new MultimediaObject(new File("C:\\Users\\SQ\\Desktop\\20220508131342\\G.E.M.邓紫棋-龙卷风.mp3"));
        MultimediaInfo info = multimediaObject.getInfo();
        Map<String, String> metadata = info.getMetadata();
        Set<String> strings = metadata.keySet();
        for (String string : strings) {
            System.out.println("key:  "+string  +"----->"+metadata.get(string));
        }
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("title","13212321");
        info.setMetadata(objectObjectHashMap);



    }
    @Test
    public void downloadUrl(){
        String downloadurl = "http://nmobi.kuwo.cn/mobi.s?f=kuwo&q=";
        String s = "user=e3cc098fd4c59ce2&android_id=e3cc098fd4c59ce2&prod=kwplayer_ar_9.3.1.3&corp=kuwo&newver=2&vipver=9.3.1.3&source=kwplayer_ar_9.3.1.3_qq.apk&p2p=1&notrace=0&type=convert_url2&br=#{brvalue}&format=flac|mp3|aac&sig=0&rid=#{musicId}&priority=bitrate&loginUid=435947810&network=WIFI&loginSid=1694167478&mode=download&uid=658048466";
        try {
            s = s.replaceAll("#\\{musicId}","184274130").replaceAll("#\\{brvalue}","2000");
            byte[] bytes = KuwoDES.encrypt2(s.getBytes("UTF-8"), s.length(), KuwoDES.SECRET_KEY, KuwoDES.SECRET_KEY_LENG);
            char[] encode = Base64Coder.encode(bytes);
            String out =  new String(encode);
            downloadurl =  downloadurl+out;
        } catch (UnsupportedEncodingException e) {
//            log.error("获取下载链接失败：{}",e.getMessage());
            return ;
        }
        String s1 = DownloadUtils.getHttp().sync(downloadurl).get().getBody().toByteString().utf8();
        System.out.println(s1);
//        downloadurl= s1.split("\n")[2].split("=")[1].split("\r")[0];
    }

}
