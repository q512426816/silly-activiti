package com.iqiny.silly.activiti;

import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.service.base.AbstractSillyReadService;
import org.springframework.beans.factory.InitializingBean;

/**
 * 结合工作流Activiti的傻瓜读取服务
 *
 * @param <M>
 * @param <N>
 * @param <V>
 */
public abstract class EnhanceSillyReadService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable>
        extends AbstractSillyReadService<M, N, V> implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        super.init();
    }

}
