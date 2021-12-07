/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-spring
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.spring.scan;

import com.iqiny.silly.core.base.SillyContext;
import com.iqiny.silly.spring.SpringSillyConfigContent;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.SillyReflectUtil;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyCategory;
import com.iqiny.silly.core.base.SillyProperties;
import com.iqiny.silly.core.base.core.SillyEntity;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.common.SillyCoreUtil;
import com.iqiny.silly.core.resume.SillyResume;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.util.*;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

@SuppressWarnings("all")
public class AutoScanSillyConfigContent extends SpringSillyConfigContent {

    private final static Log log = LogFactory.getLog(AutoScanSillyConfigContent.class);

    protected static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
    protected static final MetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory();

    protected String entityScanPackage;

    protected Class<? extends SillyMaster> masterSuperType = SillyMaster.class;
    protected Class<? extends SillyNode> nodeSuperType = SillyNode.class;
    protected Class<? extends SillyVariable> variableSuperType = SillyVariable.class;
    protected Class<? extends SillyResume> resumeSuperType = SillyResume.class;

    protected final List<Class<? extends SillyMaster>> masterClazzList = new ArrayList<>();
    protected final List<Class<? extends SillyNode>> nodeClazzList = new ArrayList<>();
    protected final List<Class<? extends SillyVariable>> variableClazzList = new ArrayList<>();
    protected final List<Class<? extends SillyResume>> resumeClazzList = new ArrayList<>();

    public AutoScanSillyConfigContent(SillyProperties sillyProperties, SillyContext sillyContext) {
        super(sillyProperties, sillyContext);
    }


    @Override
    protected void preInit() {
        super.preInit();
        this.entityScanPackage = sillyProperties.getEntityScanPackage();
        this.masterSuperType = sillyProperties.getMasterSuperType();
        this.nodeSuperType = sillyProperties.getNodeSuperType();
        this.variableSuperType = sillyProperties.getVariableSuperType();
        this.resumeSuperType = sillyProperties.getResumeSuperType();
    }

    @Override
    protected void initFiled() {
        masterClazzList.clear();
        nodeClazzList.clear();
        variableClazzList.clear();
        resumeClazzList.clear();

        super.initFiled();
    }


    @Override
    protected void initBaseSillyFactoryList() {

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
        }

        Set<String> categorys = allCategorySet();
        for (String category : categorys) {
            ScanSillyFactory sillyFactory = new ScanSillyFactory(category);
            Class<? extends SillyMaster> masterClass = SillyCoreUtil.availableOne(category, masterClazzList);
            sillyFactory.setMasterClazz(masterClass);
            Class<? extends SillyNode> nodeClass = SillyCoreUtil.availableOne(category, nodeClazzList);
            sillyFactory.setNodeClazz(nodeClass);
            Class<? extends SillyVariable> variableClazz = SillyCoreUtil.availableOne(category, variableClazzList);
            sillyFactory.setVariableClazz(variableClazz);
            Class<? extends SillyResume> resumeClazz = SillyCoreUtil.availableOne(category, resumeClazzList);
            sillyFactory.setResumeClazz(resumeClazz);
            addSillyFactory(sillyFactory);
        }
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
                    Class<?> clazz = Class.forName(classMetadata.getClassName());
                    for (Class<? extends SillyEntity> assignableType : assignableTypes) {
                        if (assignableType.isAssignableFrom(clazz)) {
                            if (masterSuperType.isAssignableFrom(clazz)) {
                                masterClazzList.add((Class<SillyMaster>) clazz);
                            } else if (nodeSuperType.isAssignableFrom(clazz)) {
                                nodeClazzList.add((Class<SillyNode>) clazz);
                            } else if (variableSuperType.isAssignableFrom(clazz)) {
                                variableClazzList.add((Class<SillyVariable>) clazz);
                            } else if (resumeSuperType.isAssignableFrom(clazz)) {
                                resumeClazzList.add((Class<SillyResume>) clazz);
                            }

                            if (SillyCategory.class.isAssignableFrom(clazz)) {
                                addCategorySet(((SillyCategory) SillyReflectUtil.newInstance(clazz)).usedCategory());
                            }
                        }
                    }

                } catch (Throwable e) {
                    log.warn(resource + ",加载失败:" + e.getMessage());
                }
            }
        }
    }

}
