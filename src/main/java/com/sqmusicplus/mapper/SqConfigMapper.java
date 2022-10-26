package com.sqmusicplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqmusicplus.entity.SqConfig;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;


/**
 * @Classname SqConfigMapper
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/10/21 10:45
 * @Created by SQ
 */
@Mapper
@CacheNamespace(blocking = false)
public interface SqConfigMapper extends BaseMapper<SqConfig> {
}