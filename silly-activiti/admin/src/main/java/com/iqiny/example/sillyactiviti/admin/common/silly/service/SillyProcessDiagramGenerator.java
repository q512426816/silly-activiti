/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.silly.service;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SillyProcessDiagramGenerator {

    private static final String FONT_NAME = "微软雅黑";

    public static InputStream generateDiagram(BpmnModel bpmnModel) {
        return generateDiagram(bpmnModel, new ArrayList<>(), new ArrayList<>());
    }

    public static InputStream generateDiagram(BpmnModel bpmnModel, List<String> highLightedActivities, List<String> highLightedFlows) {
        return new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, highLightedActivities, highLightedFlows, FONT_NAME, FONT_NAME, FONT_NAME);
    }

}
