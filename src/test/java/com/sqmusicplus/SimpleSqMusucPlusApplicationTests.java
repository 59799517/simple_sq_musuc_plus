package com.sqmusicplus;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.io.File;
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

}
