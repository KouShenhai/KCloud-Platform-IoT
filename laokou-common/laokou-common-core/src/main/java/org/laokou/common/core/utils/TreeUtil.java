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
package org.laokou.common.core.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.dto.ClientObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.laokou.common.i18n.common.Constant.COMMA;

/**
 * @author laokou
 */
@Data
public class TreeUtil {

	public static <T> TreeNode<T> rootRootNode(String name) {
		return new TreeNode<>(0L, name, null, "0", new ArrayList<>(0));
	}

	public static <T> TreeNode<T> rootRootNode() {
		return rootRootNode("根节点");
	}

	public static <T extends TreeNode<T>> T buildTreeNode(List<T> treeNodes, Class<T> clazz) {
		return buildTreeNode(treeNodes, ConvertUtil.sourceToTarget(rootRootNode(), clazz));
	}

	public static <T extends TreeNode<T>> T buildTreeNode(List<T> treeNodes, T rootNode) {
		if (null == rootNode) {
			throw new GlobalException("请构造根节点");
		}
		List<T> nodes = new ArrayList<>(treeNodes);
		nodes.add(rootNode);
		// list转map
		Map<Long, T> nodeMap = new LinkedHashMap<>(nodes.size());
		for (T node : nodes) {
			nodeMap.put(node.getId(), node);
		}
		for (T treeNo : nodes) {
			T parent = nodeMap.get(treeNo.getPid());
			if (parent != null && treeNo.getPid().equals(parent.getId())) {
				treeNo.setPath(parent.getPath() + COMMA + treeNo.getId());
				parent.getChildren().add(treeNo);
			}
		}
		return rootNode;
	}

	public static <T extends TreeNode<T>> List<T> buildTreeNode(List<T> treeNodes) {
		List<T> nodes = new ArrayList<>(treeNodes.size());
		// list转map
		Map<Long, T> nodeMap = new LinkedHashMap<>(treeNodes.size());
		for (T node : treeNodes) {
			nodeMap.put(node.getId(), node);
		}
		for (T treeNo : treeNodes) {
			T parent = nodeMap.get(treeNo.getPid());
			if (parent != null && treeNo.getPid().equals(parent.getId())) {
				treeNo.setPath(parent.getPath() + COMMA + treeNo.getId());
				parent.getChildren().add(treeNo);
				continue;
			}
			nodes.add(treeNo);
		}
		return nodes;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(name = "TreeNode", description = "树节点")
	public static class TreeNode<T> extends ClientObject {

		@Schema(name = "id", description = "ID")
		private Long id;

		@Schema(name = "name", description = "名称")
		private String name;

		@Schema(name = "pid", description = "父ID")
		private Long pid;

		@Schema(name = "path", description = "节点")
		private String path;

		@Schema(name = "children", description = "子节点")
		private List<T> children = new ArrayList<>(0);

	}

}
