package com.sqmusicplus.plug.aria2.aria2.dto.form;


import com.sqmusicplus.plug.aria2.aria2.dto.base.Aria2Option;
import com.sqmusicplus.plug.aria2.aria2.dto.base.Aria2Param;
import com.sqmusicplus.plug.aria2.aria2.enums.Aria2Method;
import com.sqmusicplus.plug.aria2.aria2.response.clazz.Aria2Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * 添加下载任务参数
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/16 11:20
 */
@RequiredArgsConstructor
@Getter
public class Aria2AddUriForm {
    final Collection<String> urls;
    final Aria2Option params;

    public Aria2Param buildParam(){
        return new Aria2Param(Aria2Method.addUri, urls, params);
    }

    public static class Response extends Aria2Response<List<List<String>>> {
    }
}
