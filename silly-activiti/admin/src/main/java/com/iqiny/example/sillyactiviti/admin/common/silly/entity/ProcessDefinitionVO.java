/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.silly.entity;

import lombok.Data;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class ProcessDefinitionVO {

    private String id;
    private String deploymentId;
    private String key;
    private String name;
    private String resourceName;
    private String category;
    private String description;
    private String version;
    private String deployTimeStr;


    public void copyFields(ProcessDefinition definition, Deployment deployment) {
        if (definition == null) {
            return;
        }

        if (deployment != null) {
            final Date deploymentTime = deployment.getDeploymentTime();
            this.deployTimeStr = deploymentTime == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(deploymentTime);
        }

        this.id = definition.getId();
        this.deploymentId = definition.getDeploymentId();
        this.key = definition.getKey();
        this.name = definition.getName();
        this.resourceName = definition.getResourceName();
        this.category = definition.getCategory();
        this.description = definition.getDescription();
        this.version = String.valueOf(definition.getVersion());
    }
}
