package com.sqmusicplus.v3.config;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.autoconfigure.DdlApplicationRunner;
import com.baomidou.mybatisplus.extension.ddl.IDdl;
import com.baomidou.mybatisplus.extension.ddl.SimpleDdl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Classname AutoDdlConfig
 * @Description 自动更新维护数据库
 * @Version 1.0.0
 * @Date 2024/8/13 20:06
 * @Created by SQ
 */
@Component
public class AutoDdlConfig   extends SimpleDdl {
    @Override
    public List<String> getSqlFiles() {
        return CollUtil.newArrayList(
                "db/sqlInit.sql"
                ,"db/sq_config.sql"
                ,"db/3.0.17update.sql"
                ,"db/3.0.21update.sql"
                ,"db/3.0.32update.sql"
                ,"db/sq_ali_sync.sql"
                ,"db/3.1.0update.sql"
                ,"db/3.1.2update.sql"
                ,"db/3.1.3update.sql"
                ,"db/3.1.4update.sql"
                ,"db/3.1.6update.sql"
                ,"db/3.1.9update.sql"
                ,"db/3.1.16update.sql"
                ,"db/3.1.17update.sql"
                ,"db/3.1.23update.sql"
                , "db/3.1.28update.sql"
        );
    }

    @Bean("ddlApplicationRunner")
    public DdlApplicationRunner ddlApplicationRunner(@Autowired(required = false) List<IDdl> ddlList) {
        return new SQDdlApplicationRunner(ddlList);
    }
}
