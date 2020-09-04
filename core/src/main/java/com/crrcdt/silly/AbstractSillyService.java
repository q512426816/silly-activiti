package com.crrcdt.silly;

import com.crrcdt.silly.base.core.SillyMaster;
import com.crrcdt.silly.base.core.SillyNode;
import com.crrcdt.silly.base.core.SillyVariable;
import com.crrcdt.silly.resume.SillyResume;
import com.crrcdt.silly.resume.SillyResumeService;

import java.util.List;

public abstract class AbstractSillyService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable> {

    private volatile AbstractSillyFactory<M, N, V> sillyFactory;

    protected SillyResumeService sillyResumeService;

    public AbstractSillyFactory<M, N, V> getSillyFactory() {
        if (sillyFactory == null) {
            synchronized (this) {
                if (sillyFactory == null) {
                    sillyFactory = createSillyFactory();
                }
            }
            if (sillyFactory == null) {
                throw new RuntimeException("Silly工厂未设置！");
            }
        }

        return sillyFactory;
    }

    protected abstract AbstractSillyFactory<M, N, V> createSillyFactory();

    public SillyResumeService getSillyResumeService() {
        if (sillyResumeService == null) {
            synchronized (this) {
                if (sillyResumeService == null) {
                    sillyResumeService = initSillyResumeService();
                }
            }
            if (sillyResumeService == null) {
                throw new RuntimeException("流程履历Service未能正确设置初始化");
            }
        }
        return sillyResumeService;
    }

    protected abstract SillyResumeService initSillyResumeService();

    public List<? extends SillyResume> getProcessInfo(String masterId, String businessType) {
        return getSillyResumeService().selectList(masterId, businessType);
    }
}
