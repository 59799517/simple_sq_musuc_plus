package com.sqmusicplus.plug.aria2.aria2.response.result;

import com.sqmusicplus.plug.aria2.aria2.response.clazz.Aria2Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/10/21 15:39
 */
@Getter
@Setter
public class Aria2Version {
    List<String > enabledFeatures;
    String version;

    public static class Response extends Aria2Response<Aria2Version> {
    }
}
