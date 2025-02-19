/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.ClientObject;
import org.laokou.common.i18n.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 树节菜单工具类.
 *
 * @author laokou
 */
public final class TreeUtil {

	private TreeUtil() {
	}

	/**
	 * 构建树节点.
	 * @param treeNodes 菜单列表
	 * @param clazz 类
	 * @param <T> 泛型
	 * @return 树节点
	 */
	public static <T extends TreeNode<T>> T buildTreeNode(List<T> treeNodes, Class<T> clazz) {
		return buildTreeNode(treeNodes, ConvertUtil.sourceToTarget(rootNode(), clazz));
	}

	/**
	 * 顶级树节点.
	 * @param <T> 泛型
	 * @return 顶级菜单节点
	 */
	private static <T> TreeNode<T> rootNode() {
		return new TreeNode<>(0L, "根节点", null);
	}

	/**
	 * 构建树节点.
	 * @param treeNodes 菜单列表
	 * @param rootNode 顶级节点
	 * @param <T> 泛型
	 * @return 树节点
	 */
	private static <T extends TreeNode<T>> T buildTreeNode(List<T> treeNodes, T rootNode) {
		if (ObjectUtil.isNull(rootNode)) {
			throw new RuntimeException("请构造根节点");
		}
		List<T> nodes = new ArrayList<>(treeNodes);
		// 添加根节点
		nodes.add(rootNode);
		// list转map
		Map<Long, T> nodeMap = new LinkedHashMap<>(nodes.size());
		for (T node : nodes) {
			nodeMap.put(node.getId(), node);
		}
		for (T node : nodes) {
			T parentNode = nodeMap.get(node.getPid());
			if (ObjectUtil.isNotNull(parentNode) && ObjectUtil.equals(node.getPid(), parentNode.getId())) {
				parentNode.getChildren().add(node);
			}
		}
		return rootNode;
	}

	@Data
	@NoArgsConstructor
	@Schema(name = "树节点", description = "树节点")
	public static class TreeNode<T> extends ClientObject {

		@Schema(name = "ID", description = "ID")
		protected Long id;

		@Schema(name = "名称", description = "名称")
		protected String name;

		@Schema(name = "父节点ID", description = "父节点ID")
		protected Long pid;

		@Schema(name = "子节点", description = "子节点")
		protected List<T> children = new ArrayList<>(16);

		public TreeNode(Long id, String name, Long pid) {
			this.id = id;
			this.name = name;
			this.pid = pid;
		}

	}

}
