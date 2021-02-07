package com.iqiny.example.sillyactiviti.admin.modules.sys.redis;


import com.iqiny.example.sillyactiviti.admin.common.utils.RedisKeys;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysConfigEntity;
import org.springframework.stereotype.Component;

/**
 * 系统配置Redis
 *
 * @author Mark sunlightcs@gmail.com
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
