/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.utils.TreeUtil;

import java.util.ArrayList;
import java.util.List;

class TreeUtilTest {

	@Test
	void testTree() {
		Node n1 = new Node(1L, "节点1", 0L);
		Node n2 = new Node(11L, "节点1-1", 1L);
		Node node = TreeUtil.buildTreeNode(List.of(n1, n2), Node.class);
		Assertions.assertEquals(node.getId(), node.getChildren().getFirst().getPid());
	}

	@Data
	@NoArgsConstructor
	static class Node extends TreeUtil.TreeNode<Node> {

		@JsonIgnore
		private Long id;

		@JsonProperty("routers")
		private List<Node> children = new ArrayList<>(16);

		public Node(Long id, String name, Long pid) {
			super(id, name, pid);
		}

	}

}
