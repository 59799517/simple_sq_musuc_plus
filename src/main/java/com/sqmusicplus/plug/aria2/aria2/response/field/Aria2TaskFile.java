package com.sqmusicplus.plug.aria2.aria2.response.field;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/10/21 14:40
 */
@Getter
@Setter
public class Aria2TaskFile {
    String path;
    Long completedLength;
    Long length;
    Integer index;
    Boolean selected;
    List<Uri> uris;

    @Data
    public static class Uri {
        String uri;
        String status;
    }
}
