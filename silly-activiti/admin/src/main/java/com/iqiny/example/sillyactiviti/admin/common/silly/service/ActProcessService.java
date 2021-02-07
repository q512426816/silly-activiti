package com.iqiny.example.sillyactiviti.admin.common.silly.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iqiny.example.sillyactiviti.admin.common.silly.config.SillyProcessDiagramGenerator;
import com.iqiny.example.sillyactiviti.admin.common.silly.entity.ProcessDefinitionVO;
import com.iqiny.example.sillyactiviti.admin.common.utils.Query;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;
import com.iqiny.example.sillyactiviti.common.utils.StringUtils;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * 流程定义
 */
@Service
public class ActProcessService {

    public static final String IMAGE = "image";
    public static final String XML = "xml";

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 流程定义列表
     */
    public PageUtils queryPage(Map<String, Object> params) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .latestVersion().orderByProcessDefinitionKey().asc();

        String category = (String) params.get("category");
        if (StringUtils.isNotEmpty(category)) {
            processDefinitionQuery.processDefinitionCategory(category);
        }
        IPage<ProcessDefinitionVO> page = makeNewPage(params);
        final long total = processDefinitionQuery.count();
        page.setTotal(total);
        int limit = (int) page.getSize();
        int offset = (int) (limit * page.getCurrent() - limit);
        final List<ProcessDefinition> processDefinitions = processDefinitionQuery.listPage(offset, limit);
        List<ProcessDefinitionVO> returnList = new ArrayList<>();
        for (ProcessDefinition definition : processDefinitions) {
            final ProcessDefinitionVO processDefinitionEntity = new ProcessDefinitionVO();
            Deployment deployment = null;
            if (definition != null && definition.getDeploymentId() != null) {
                deployment = repositoryService.createDeploymentQuery().deploymentId(definition.getDeploymentId()).singleResult();
            }
            processDefinitionEntity.copyFields(definition, deployment);
            returnList.add(processDefinitionEntity);
        }
        page.setRecords(returnList);
        return new PageUtils(page);
    }

    protected IPage<ProcessDefinitionVO> makeNewPage(Map<String, Object> params) {
        return new Query<ProcessDefinitionVO>().getPage(params);
    }

    /**
     * 读取资源，通过部署ID
     */
    public InputStream resourceRead(String processDefinitionId, String resType) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        if(processDefinition == null){
            return null;
        }
        String resourceName = "";
        if (IMAGE.equals(resType)) {
            resourceName = processDefinition.getDiagramResourceName();
            if (StringUtils.isEmpty(resourceName)) {
                return gen(processDefinitionId);
            }
        } else if (XML.equals(resType)) {
            resourceName = processDefinition.getResourceName();
        }

        if (StringUtils.isEmpty(resourceName)) {
            return null;
        }
        return repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
    }


    public InputStream gen(String processDefinitionId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        return SillyProcessDiagramGenerator.generateDiagram(bpmnModel);
    }


    /**
     * 部署流程 - 保存
     *
     * @param file
     * @return
     */
    @Transactional(readOnly = false)
    public void deploy(String category, MultipartFile file) {

        String fileName = file.getOriginalFilename();

        try {
            InputStream fileInputStream = file.getInputStream();
            Deployment deployment = null;
            String extension = FilenameUtils.getExtension(fileName);
            if ("zip".equals(extension) || "bar".equals(extension)) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
            } else if ("png".equals(extension)) {
                deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            } else if (fileName.contains("bpmn20.xml")) {
                deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            } else if ("bpmn".equals(extension)) {
                String baseName = FilenameUtils.getBaseName(fileName);
                deployment = repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml", fileInputStream).deploy();
            } else {
                throw new ActivitiException("不支持的文件类型：" + extension);
            }

            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();

            // 设置流程分类
            for (ProcessDefinition processDefinition : list) {
                repositoryService.setProcessDefinitionCategory(processDefinition.getId(), category);
            }

            if (list.size() == 0) {
                throw new ActivitiException("无流程！");
            }

        } catch (Exception e) {
            throw new ActivitiException("部署失败！", e);
        }
    }

    /**
     * 设置流程分类
     */
    @Transactional(readOnly = false)
    public void updateCategory(String procDefId, String category) {
        repositoryService.setProcessDefinitionCategory(procDefId, category);
    }

    /**
     * 挂起、激活流程实例
     */
    @Transactional(readOnly = false)
    public String updateState(String state, String procDefId) {
        if (state.equals("active")) {
            repositoryService.activateProcessDefinitionById(procDefId, true, null);
            return "已激活ID为[" + procDefId + "]的流程定义。";
        } else if (state.equals("suspend")) {
            repositoryService.suspendProcessDefinitionById(procDefId, true, null);
            return "已挂起ID为[" + procDefId + "]的流程定义。";
        }
        return "无操作";
    }

    /**
     * 删除部署的流程，级联删除流程实例
     *
     * @param deploymentId 流程部署ID
     */
    @Transactional(readOnly = false)
    public void deleteDeployment(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * 删除部署的流程实例
     *
     * @param procInsId    流程实例ID
     * @param deleteReason 删除原因，可为空
     */
    @Transactional(readOnly = false)
    public void deleteProcIns(String procInsId, String deleteReason) {
        runtimeService.deleteProcessInstance(procInsId, deleteReason);
    }

}
