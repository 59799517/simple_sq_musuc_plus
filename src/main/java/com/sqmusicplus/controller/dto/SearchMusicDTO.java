package com.sqmusicplus.controller.dto;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @Classname SearchMusicVo
 * @Description 搜索
 * @Version 1.0.0
 * @Date 2022/6/2 13:46
 * @Created by SQ
 */
@Data
public class SearchMusicDTO {
    String keyword = "";
    @Size(max = 50)
    Integer pageSize =20;
    @Size(min = 1)
    Integer pageIndex =1;
}
