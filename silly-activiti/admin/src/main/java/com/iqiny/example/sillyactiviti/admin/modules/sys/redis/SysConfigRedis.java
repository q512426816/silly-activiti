/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.sys.redis;


import com.iqiny.example.sillyactiviti.admin.common.utils.RedisKeys;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysConfigEntity;
import org.springframework.stereotype.Component;

/**
 * 系统配置Redis
 *
 *
 */
@Component
public class SysConfigRedis {
    //@Autowired
    //private RedisUtils redisUtils;

    public void saveOrUpdate(SysConfigEntity config) {
        if(config == null){
            return ;
        }
        String key = RedisKeys.getSysConfigKey(config.getParamKey());
        //redisUtils.set(key, config);
    }

    public void delete(String configKey) {
        String key = RedisKeys.getSysConfigKey(configKey);
        //redisUtils.delete(key);
    }

    public SysConfigEntity get(String configKey){
        String key = RedisKeys.getSysConfigKey(configKey);
        return null;//redisUtils.get(key, SysConfigEntity.class);
    }
}
