package com.sqmusicplus.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @Classname ListenerEntity
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/8/10 16:09
 * @Created by SQ
 */
@Data
@Accessors(chain = true)
@ToString
public class ParserEntity {
    String songName;
    String artistsName;
    String sourceName;
}
