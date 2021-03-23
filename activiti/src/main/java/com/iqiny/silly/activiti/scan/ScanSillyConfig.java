/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-activiti 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.activiti.scan;

import com.iqiny.silly.activiti.ActivitiSillyConfig;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.SillyReflectUtil;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyEntity;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.resume.SillyResume;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

public class ScanSillyConfig extends ActivitiSillyConfig {

    private final static Log log = LogFactory.getLog(ScanSillyConfig.class);

    protected static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
    protected static final MetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory();

    protected String entityScanPackage;

    protected Class<SillyMaster> masterSuperType = SillyMaster.class;
    protected Class<SillyNode> nodeSuperType = SillyNode.class;
    protected Class<SillyVariable> variableSuperType = SillyVariable.class;
    protected Class<SillyResume> resumeSuperType = SillyResume.class;

    protected Map<String, ScanSillyFactory> scanSillyFactoryMap = new LinkedHashMap<>();


    @Override
    protected void preInit() {
        super.preInit();

        if (StringUtils.isNotEmpty(entityScanPackage)) {
            Set<Class<? extends SillyEntity>> types = new LinkedHashSet<>();
            types.add(masterSuperType);
            types.add(nodeSuperType);
            types.add(variableSuperType);
            types.add(resumeSuperType);
            try {
                scanRefreshClasses(entityScanPackage, types);
            } catch (IOException e) {
                log.warn("包扫描异常！" + entityScanPackage + "类型：" + types + e.getMessage());
            }

            initSupportCategories();
        }
    }

    protected void initSupportCategories() {
        // 若没有设置支持的种类，则将全部扫描到的种类都设置进去 初始化 支持种类
        if (supportCategories.isEmpty()) {
            supportCategories.addAll(scanSillyFactoryMap.keySet());
        }
    }

    @Override
    protected void initBaseSillyFactoryMap() {
        super.initBaseSillyFactoryMap();

        for (String category : supportCategories) {
            final ScanSillyFactory scanSillyFactory = scanSillyFactoryMap.get(category);
            addSillyFactory(scanSillyFactory);
        }
    }

    public void setEntityScanPackage(String entityScanPackage) {
        this.entityScanPackage = entityScanPackage;
    }

    /**
     * 借鉴 MyBatis 扫描包的方法
     *
     * @param packagePatterns 包扫描表达式
     * @param assignableTypes 基本数据类型
     * @return
     * @throws IOException
     */
    private void scanRefreshClasses(String packagePatterns, Set<Class<? extends SillyEntity>> assignableTypes)
            throws IOException {
        SillyAssert.notEmpty(packagePatterns, "包扫描表达式不可为空");
        SillyAssert.notNull(assignableTypes);

        String[] packagePatternArray = tokenizeToStringArray(packagePatterns,
                ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        for (String packagePattern : packagePatternArray) {
            Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    org.springframework.util.ClassUtils.convertClassNameToResourcePath(packagePattern) + "/**/*.class");
            for (Resource resource : resources) {
                try {
                    ClassMetadata classMetadata = METADATA_READER_FACTORY.getMetadataReader(resource).getClassMetadata();
                    Class<?> clazz = Resources.classForName(classMetadata.getClassName());
                    for (Class<? extends SillyEntity> assignableType : assignableTypes) {
                        if (assignableType.isAssignableFrom(clazz)) {
                            SillyEntity sillyEntity = (SillyEntity) SillyReflectUtil.newInstance(clazz);
                            final String category = sillyEntity.category();
                            ScanSillyFactory sillyFactory = scanSillyFactoryMap.get(category);
                            if (sillyFactory == null) {
                                sillyFactory = new ScanSillyFactory(category);
                                scanSillyFactoryMap.put(category, sillyFactory);
                            }
                            sillyFactory.setEntityClazz(clazz);
                        }
                    }

                } catch (Throwable e) {
                    log.warn(resource + ",加载失败:" + e.getMessage());
                }
            }
        }

        // 设置 特殊种类的操作
        refreshSpecialSillyFactory();
    }

    protected void refreshSpecialSillyFactory() {
        final ScanSillyFactory supportAll = scanSillyFactoryMap.get(SillyEntity.SUPPORT_ALL);
        if (supportAll == null) {
            return;
        }
        
        for (String key : scanSillyFactoryMap.keySet()) {
            scanSillyFactoryMap.get(key).setDefaultClazz(supportAll);
        }
    }

}
