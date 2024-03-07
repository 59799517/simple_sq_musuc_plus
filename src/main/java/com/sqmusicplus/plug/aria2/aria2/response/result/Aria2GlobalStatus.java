package com.sqmusicplus.plug.aria2.aria2.response.result;

import com.sqmusicplus.plug.aria2.aria2.response.clazz.Aria2Response;
import lombok.Getter;
import lombok.Setter;

/**
 * 概况统计
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/10/21 15:20
 */
@Getter
@Setter
public class Aria2GlobalStatus {
    Integer numActive;
    Integer numWaiting;
    Long downloadSpeed;
    Long uploadSpeed;
    Integer numStopped;
    Integer numStoppedTotal;

    public static class Response extends Aria2Response<Aria2GlobalStatus> {
    }
}
