/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.laokou.flowable.config;

import lombok.Getter;
import lombok.Setter;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.image.impl.DefaultProcessDiagramCanvas;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.laokou.common.core.utils.BigDecimalUtil;

import java.util.Iterator;
import java.util.List;

/**
 * @author laokou
 */
public class ProcessDiagramGeneratorConfig extends DefaultProcessDiagramGenerator {

	@Override
	protected DefaultProcessDiagramCanvas generateProcessDiagram(BpmnModel bpmnModel, String imageType,
			List<String> highLightedActivities, List<String> highLightedFlows, String activityFontName,
			String labelFontName, String annotationFontName, ClassLoader customClassLoader, double scaleFactor,
			boolean drawSequenceFlowNameWithNoLabelDi) {
		this.prepareBpmnModel(bpmnModel);
		DefaultProcessDiagramCanvas processDiagramCanvas = initProcessDiagramCanvas(bpmnModel, imageType,
				activityFontName, labelFontName, annotationFontName, customClassLoader);
		Iterator var13 = bpmnModel.getPools().iterator();

		while (var13.hasNext()) {
			Pool process = (Pool) var13.next();
			GraphicInfo subProcesses = bpmnModel.getGraphicInfo(process.getId());
			processDiagramCanvas.drawPoolOrLane(process.getName(), subProcesses, scaleFactor);
		}
		var13 = bpmnModel.getProcesses().iterator();
		Process process1;
		Iterator subProcesses1;
		while (var13.hasNext()) {
			process1 = (Process) var13.next();
			subProcesses1 = process1.getLanes().iterator();

			while (subProcesses1.hasNext()) {
				Lane artifact = (Lane) subProcesses1.next();
				GraphicInfo subProcess = bpmnModel.getGraphicInfo(artifact.getId());
				processDiagramCanvas.drawPoolOrLane(artifact.getName(), subProcess, scaleFactor);
			}
		}
		var13 = bpmnModel.getProcesses().iterator();
		while (var13.hasNext()) {
			process1 = (Process) var13.next();
			subProcesses1 = process1.findFlowElementsOfType(FlowNode.class).iterator();

			while (subProcesses1.hasNext()) {
				FlowNode artifact1 = (FlowNode) subProcesses1.next();
				if (!this.isPartOfCollapsedSubProcess(artifact1, bpmnModel)) {
					this.drawActivity(processDiagramCanvas, bpmnModel, artifact1, highLightedActivities,
							highLightedFlows, scaleFactor, drawSequenceFlowNameWithNoLabelDi);
				}
			}
		}
		var13 = bpmnModel.getProcesses().iterator();
		label75: while (true) {
			List subProcesses2;
			do {
				if (!var13.hasNext()) {
					return processDiagramCanvas;
				}
				process1 = (Process) var13.next();
				subProcesses1 = process1.getArtifacts().iterator();
				while (subProcesses1.hasNext()) {
					Artifact artifact2 = (Artifact) subProcesses1.next();
					this.drawArtifact(processDiagramCanvas, bpmnModel, artifact2);
				}
				subProcesses2 = process1.findFlowElementsOfType(SubProcess.class, true);
			}
			while (subProcesses2 == null);
			Iterator artifact3 = subProcesses2.iterator();
			while (true) {
				GraphicInfo graphicInfo;
				SubProcess subProcess1;
				do {
					do {
						if (!artifact3.hasNext()) {
							continue label75;
						}
						subProcess1 = (SubProcess) artifact3.next();
						graphicInfo = bpmnModel.getGraphicInfo(subProcess1.getId());
					}
					while (graphicInfo != null && graphicInfo.getExpanded() != null && !graphicInfo.getExpanded());
				}
				while (this.isPartOfCollapsedSubProcess(subProcess1, bpmnModel));
				for (Artifact subProcessArtifact : subProcess1.getArtifacts()) {
					this.drawArtifact(processDiagramCanvas, bpmnModel, subProcessArtifact);
				}
			}
		}
	}

	protected static DefaultProcessDiagramCanvas initProcessDiagramCanvas(BpmnModel bpmnModel, String imageType,
			String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader) {
		double minX = 1.7976931348623157E308D;
		double maxX = 0.0D;
		double minY = 1.7976931348623157E308D;
		double maxY = 0.0D;
		GraphicInfo nrOfLanes;
		for (Iterator flowNodes = bpmnModel.getPools().iterator(); flowNodes
			.hasNext(); maxY = nrOfLanes.getY() + nrOfLanes.getHeight()) {
			Pool artifacts = (Pool) flowNodes.next();
			nrOfLanes = bpmnModel.getGraphicInfo(artifacts.getId());
			minX = nrOfLanes.getX();
			maxX = nrOfLanes.getX() + nrOfLanes.getWidth();
			minY = nrOfLanes.getY();
		}
		return initProcessDiagramCanvas(minX, maxX, minY, maxY, bpmnModel, imageType, activityFontName, labelFontName,
				annotationFontName, customClassLoader);
	}

	private static ProcessNum getProcessNum1(double minX, double maxX, double minY, double maxY, BpmnModel bpmnModel,
			List var23) {
		Iterator var24 = var23.iterator();
		label155: while (var24.hasNext()) {
			FlowNode var26 = (FlowNode) var24.next();
			GraphicInfo artifact = bpmnModel.getGraphicInfo(var26.getId());
			if (artifact.getX() + artifact.getWidth() > maxX) {
				maxX = artifact.getX() + artifact.getWidth();
			}
			if (artifact.getX() < minX) {
				minX = artifact.getX();
			}
			if (artifact.getY() + artifact.getHeight() > maxY) {
				maxY = artifact.getY() + artifact.getHeight();
			}
			if (artifact.getY() < minY) {
				minY = artifact.getY();
			}
			Iterator process = var26.getOutgoingFlows().iterator();
			while (true) {
				List l;
				do {
					if (!process.hasNext()) {
						continue label155;
					}
					SequenceFlow graphicInfoList = (SequenceFlow) process.next();
					l = bpmnModel.getFlowLocationGraphicInfo(graphicInfoList.getId());
				}
				while (l == null);
				for (Object o : l) {
					GraphicInfo graphicInfo1 = (GraphicInfo) o;
					if (graphicInfo1.getX() > maxX) {
						maxX = graphicInfo1.getX();
					}
					if (graphicInfo1.getX() < minX) {
						minX = graphicInfo1.getX();
					}
					if (graphicInfo1.getY() > maxY) {
						maxY = graphicInfo1.getY();
					}
					if (graphicInfo1.getY() < minY) {
						minY = graphicInfo1.getY();
					}
				}
			}
		}
		ProcessNum processNum = new ProcessNum();
		processNum.setMinX(minX);
		processNum.setMinY(minY);
		processNum.setMaxX(maxX);
		processNum.setMaxY(maxY);
		return processNum;
	}

	private static ProcessNum getProcessNum2(double minX, double maxX, double minY, double maxY, BpmnModel bpmnModel,
			List var23) {
		List var25 = gatherAllArtifacts(bpmnModel);
		Iterator var27 = var25.iterator();
		GraphicInfo var37;
		while (var27.hasNext()) {
			Artifact var29 = (Artifact) var27.next();
			GraphicInfo var31 = bpmnModel.getGraphicInfo(var29.getId());
			if (var31 != null) {
				if (var31.getX() + var31.getWidth() > maxX) {
					maxX = var31.getX() + var31.getWidth();
				}
				if (var31.getX() < minX) {
					minX = var31.getX();
				}
				if (var31.getY() + var31.getHeight() > maxY) {
					maxY = var31.getY() + var31.getHeight();
				}
				if (var31.getY() < minY) {
					minY = var31.getY();
				}
			}
			List var33 = bpmnModel.getFlowLocationGraphicInfo(var29.getId());
			if (var33 != null) {

				for (Object o : var33) {
					var37 = (GraphicInfo) o;
					if (var37.getX() > maxX) {
						maxX = var37.getX();
					}
					if (var37.getX() < minX) {
						minX = var37.getX();
					}
					if (var37.getY() > maxY) {
						maxY = var37.getY();
					}
					if (var37.getY() < minY) {
						minY = var37.getY();
					}
				}
			}
		}
		int var28 = 0;
		for (Process var32 : bpmnModel.getProcesses()) {
			for (Lane var36 : var32.getLanes()) {
				++var28;
				var37 = bpmnModel.getGraphicInfo(var36.getId());
				if (var37.getX() + var37.getWidth() > maxX) {
					maxX = var37.getX() + var37.getWidth();
				}
				if (var37.getX() < minX) {
					minX = var37.getX();
				}
				if (var37.getY() + var37.getHeight() > maxY) {
					maxY = var37.getY() + var37.getHeight();
				}
				if (var37.getY() < minY) {
					minY = var37.getY();
				}
			}
		}
		if (var23.isEmpty() && bpmnModel.getPools().isEmpty() && var28 == 0) {
			minX = 0.0D;
			minY = 0.0D;
		}
		ProcessNum processNum = new ProcessNum();
		processNum.setMinX(minX);
		processNum.setMinY(minY);
		processNum.setMaxX(maxX);
		processNum.setMaxY(maxY);
		return processNum;
	}

	private static ProcessDiagramCanvasConfig initProcessDiagramCanvas(double minX, double maxX, double minY,
			double maxY, BpmnModel bpmnModel, String imageType, String activityFontName, String labelFontName,
			String annotationFontName, ClassLoader customClassLoader) {
		List var23 = gatherAllFlowNodes(bpmnModel);

		ProcessNum processNum1 = getProcessNum1(minX, maxX, minY, maxY, bpmnModel, var23);

		minX = processNum1.getMinX();
		minY = processNum1.getMinY();
		maxX = processNum1.getMaxX();
		maxY = processNum1.getMaxY();

		ProcessNum processNum2 = getProcessNum2(minX, maxX, minY, maxY, bpmnModel, var23);

		minX = processNum2.getMinX();
		minY = processNum2.getMinY();
		maxX = processNum2.getMaxX();
		maxY = processNum2.getMaxY();

		return new ProcessDiagramCanvasConfig((int) maxX + 10, (int) maxY + 10, (int) minX, (int) minY, imageType,
				activityFontName, labelFontName, annotationFontName, customClassLoader);
	}

	private static void drawHighLight(DefaultProcessDiagramCanvas processDiagramCanvas, GraphicInfo graphicInfo) {
		processDiagramCanvas.drawHighLight((int) graphicInfo.getX(), (int) graphicInfo.getY(),
				(int) graphicInfo.getWidth(), (int) graphicInfo.getHeight());

	}

	private static void drawHighLightNow(ProcessDiagramCanvasConfig processDiagramCanvas, GraphicInfo graphicInfo) {
		processDiagramCanvas.drawHighLightNow((int) graphicInfo.getX(), (int) graphicInfo.getY(),
				(int) graphicInfo.getWidth(), (int) graphicInfo.getHeight());

	}

	private static void drawHighLightEnd(ProcessDiagramCanvasConfig processDiagramCanvas, GraphicInfo graphicInfo) {
		processDiagramCanvas.drawHighLightEnd((int) graphicInfo.getX(), (int) graphicInfo.getY(),
				(int) graphicInfo.getWidth(), (int) graphicInfo.getHeight());

	}

	@Override
	protected void drawActivity(DefaultProcessDiagramCanvas processDiagramCanvas, BpmnModel bpmnModel,
			FlowNode flowNode, List<String> highLightedActivities, List<String> highLightedFlows, double scaleFactor,
			Boolean drawSequenceFlowNameWithNoLabelDi) {
		drawActivity(processDiagramCanvas, bpmnModel, flowNode, highLightedActivities, scaleFactor);
		// Outgoing transitions of activity
		for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
			boolean highLighted = (highLightedFlows.contains(sequenceFlow.getId()));
			String defaultFlow = null;
			if (flowNode instanceof Activity) {
				defaultFlow = ((Activity) flowNode).getDefaultFlow();
			}
			else if (flowNode instanceof Gateway) {
				defaultFlow = ((Gateway) flowNode).getDefaultFlow();
			}
			boolean isDefault = defaultFlow != null && defaultFlow.equalsIgnoreCase(sequenceFlow.getId());
			boolean drawConditionalIndicator = sequenceFlow.getConditionExpression() != null
					&& !(flowNode instanceof Gateway);
			String sourceRef = sequenceFlow.getSourceRef();
			String targetRef = sequenceFlow.getTargetRef();
			FlowElement sourceElement = bpmnModel.getFlowElement(sourceRef);
			FlowElement targetElement = bpmnModel.getFlowElement(targetRef);
			List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
			if (graphicInfoList != null && graphicInfoList.size() > 0) {
				graphicInfoList = connectionPerfectionizer(processDiagramCanvas, bpmnModel, sourceElement,
						targetElement, graphicInfoList);
				int[] xPoints = new int[graphicInfoList.size()];
				int[] yPoints = new int[graphicInfoList.size()];

				for (int i = 1; i < graphicInfoList.size(); i++) {
					GraphicInfo graphicInfo = graphicInfoList.get(i);
					GraphicInfo previousGraphicInfo = graphicInfoList.get(i - 1);

					if (i == 1) {
						xPoints[0] = (int) previousGraphicInfo.getX();
						yPoints[0] = (int) previousGraphicInfo.getY();
					}
					xPoints[i] = (int) graphicInfo.getX();
					yPoints[i] = (int) graphicInfo.getY();

				}
				processDiagramCanvas.drawSequenceflow(xPoints, yPoints, drawConditionalIndicator, isDefault,
						highLighted, scaleFactor);
				// Draw sequenceflow label
				GraphicInfo labelGraphicInfo = bpmnModel.getLabelGraphicInfo(sequenceFlow.getId());
				if (labelGraphicInfo != null) {
					processDiagramCanvas.drawLabel(sequenceFlow.getName(), labelGraphicInfo, false);
				}
				else {
					if (drawSequenceFlowNameWithNoLabelDi) {
						GraphicInfo lineCenter = getLineCenter(graphicInfoList);
						processDiagramCanvas.drawLabel(sequenceFlow.getName(), lineCenter, false);
					}
				}
			}
		}
		// Nested elements
		if (flowNode instanceof FlowElementsContainer) {
			for (FlowElement nestedFlowElement : ((FlowElementsContainer) flowNode).getFlowElements()) {
				if (nestedFlowElement instanceof FlowNode
						&& !isPartOfCollapsedSubProcess(nestedFlowElement, bpmnModel)) {
					drawActivity(processDiagramCanvas, bpmnModel, (FlowNode) nestedFlowElement, highLightedActivities,
							highLightedFlows, scaleFactor, drawSequenceFlowNameWithNoLabelDi);
				}
			}
		}
	}

	private void drawActivity(DefaultProcessDiagramCanvas processDiagramCanvas, BpmnModel bpmnModel, FlowNode flowNode,
			List<String> highLightedActivities, double scaleFactor) {
		ActivityDrawInstruction drawInstruction = activityDrawInstructions.get(flowNode.getClass());
		if (drawInstruction != null) {
			drawInstruction.draw(processDiagramCanvas, bpmnModel, flowNode);
			// Gather info on the multi instance marker
			boolean multiInstanceSequential = false;
			boolean multiInstanceParallel = false;
			boolean collapsed = false;
			if (flowNode instanceof Activity activity) {
				MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = activity.getLoopCharacteristics();
				if (multiInstanceLoopCharacteristics != null) {
					multiInstanceSequential = multiInstanceLoopCharacteristics.isSequential();
					multiInstanceParallel = !multiInstanceSequential;
				}
			}
			// Gather info on the collapsed marker
			GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
			if (flowNode instanceof SubProcess) {
				collapsed = graphicInfo.getExpanded() != null && !graphicInfo.getExpanded();
			}
			else if (flowNode instanceof CallActivity) {
				collapsed = true;
			}
			if (BigDecimalUtil.compareTo(1.0, scaleFactor) == 0) {
				// Actually draw the markers
				processDiagramCanvas.drawActivityMarkers((int) graphicInfo.getX(), (int) graphicInfo.getY(),
						(int) graphicInfo.getWidth(), (int) graphicInfo.getHeight(), multiInstanceSequential,
						multiInstanceParallel, collapsed);
			}
			// Draw highlighted activities
			if (highLightedActivities.contains(flowNode.getId())) {

				if (highLightedActivities.get(highLightedActivities.size() - 1).equals(flowNode.getId())
						&& !"endenv".equals(flowNode.getId())) {
					String eventPrefix = "Event_";
					if ((flowNode.getId().contains(eventPrefix))) {
						drawHighLightEnd((ProcessDiagramCanvasConfig) processDiagramCanvas,
								bpmnModel.getGraphicInfo(flowNode.getId()));
					}
					else {
						drawHighLightNow((ProcessDiagramCanvasConfig) processDiagramCanvas,
								bpmnModel.getGraphicInfo(flowNode.getId()));
					}
				}
				else {
					drawHighLight(processDiagramCanvas, bpmnModel.getGraphicInfo(flowNode.getId()));
				}
			}
		}
	}

	@Getter
	@Setter
	static class ProcessNum {

		private double minX;

		private double maxX;

		private double minY;

		private double maxY;

	}

}
