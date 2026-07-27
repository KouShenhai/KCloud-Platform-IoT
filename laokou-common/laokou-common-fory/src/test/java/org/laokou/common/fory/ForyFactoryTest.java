/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.fory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.laokou.common.fory.config.ForyFactory;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ForyFactory 测试类.
 *
 * @author laokou
 */
class ForyFactoryTest {

	private static ForyFactory foryFactory;

	@BeforeAll
	static void setup() {
		foryFactory = ForyFactory.INSTANCE;
		// 注册测试用的类
		foryFactory.register(User.class);
		foryFactory.register(Order.class);
		foryFactory.register(ArrayList.class);
	}

	@Test
	void testSerializeAndDeserializeNull() {
		// 测试 null 对象序列化
		byte[] result = foryFactory.serialize(null);
		Assertions.assertThat(result).isEmpty();
	}

	@Test
	void testSerializeAndDeserializeString() {
		// 测试字符串序列化
		String testString = "Hello, Fory!";
		byte[] serialized = foryFactory.serialize(testString);
		Assertions.assertThat(serialized).isEqualTo(testString.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void testSerializeAndDeserializeEmptyString() {
		// 测试空字符串序列化
		String emptyString = "";
		byte[] serialized = foryFactory.serialize(emptyString);
		Assertions.assertThat(serialized).isEqualTo(emptyString.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void testSerializeAndDeserializeUser() {
		// 测试 User 对象序列化与反序列化
		User user = new User(1L, "张三", 25, "zhangsan@example.com");
		byte[] serialized = foryFactory.serialize(user);
		Assertions.assertThat(serialized).isNotEmpty();

		Object deserialized = foryFactory.deserialize(serialized);
		Assertions.assertThat(deserialized).isInstanceOf(User.class);

		User deserializedUser = (User) deserialized;
		Assertions.assertThat(deserializedUser.getId()).isEqualTo(user.getId());
		Assertions.assertThat(deserializedUser.getName()).isEqualTo(user.getName());
		Assertions.assertThat(deserializedUser.getAge()).isEqualTo(user.getAge());
		Assertions.assertThat(deserializedUser.getEmail()).isEqualTo(user.getEmail());
	}

	@Test
	void testSerializeAndDeserializeOrder() {
		// 测试 Order 对象序列化与反序列化
		Order order = new Order("ORD-001", 199.99, Arrays.asList("商品A", "商品B", "商品C"));
		byte[] serialized = foryFactory.serialize(order);
		Assertions.assertThat(serialized).isNotEmpty();

		Object deserialized = foryFactory.deserialize(serialized);
		Assertions.assertThat(deserialized).isInstanceOf(Order.class);

		Order deserializedOrder = (Order) deserialized;
		Assertions.assertThat(deserializedOrder.getOrderId()).isEqualTo(order.getOrderId());
		Assertions.assertThat(deserializedOrder.getTotalAmount()).isEqualTo(order.getTotalAmount());
		Assertions.assertThat(deserializedOrder.getItems()).containsExactlyElementsOf(order.getItems());
	}

	@Test
	void testSerializeAndDeserializeList() {
		// 测试 List 序列化与反序列化
		List<String> list = new ArrayList<>();
		list.add("item1");
		list.add("item2");
		list.add("item3");

		byte[] serialized = foryFactory.serialize(list);
		Assertions.assertThat(serialized).isNotEmpty();

		Object deserialized = foryFactory.deserialize(serialized);
		Assertions.assertThat(deserialized).isInstanceOf(List.class);

		@SuppressWarnings("unchecked")
		List<String> deserializedList = (List<String>) deserialized;
		Assertions.assertThat(deserializedList).containsExactly("item1", "item2", "item3");
	}

	@Test
	void testGetFory() {
		// 测试获取 Fory 实例
		Assertions.assertThat(foryFactory.getFory()).isNotNull();
	}

	@Test
	void testSingletonInstance() {
		// 测试单例模式
		ForyFactory instance1 = ForyFactory.INSTANCE;
		ForyFactory instance2 = ForyFactory.INSTANCE;
		Assertions.assertThat(instance1).isSameAs(instance2);
	}

	@Test
	void testSerializeAndDeserializeChineseCharacters() {
		// 测试中文字符串序列化
		String chineseString = "你好，世界！这是一个测试。";
		byte[] serialized = foryFactory.serialize(chineseString);
		Assertions.assertThat(serialized).isEqualTo(chineseString.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void testSerializeAndDeserializeSpecialCharacters() {
		// 测试特殊字符序列化
		String specialString = "Hello\n\t\r\0World!@#$%^&*()";
		byte[] serialized = foryFactory.serialize(specialString);
		Assertions.assertThat(serialized).isEqualTo(specialString.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void testSerializeAndDeserializeUserWithNullFields() {
		// 测试包含 null 字段的对象序列化与反序列化
		User user = new User(2L, null, 30, null);
		byte[] serialized = foryFactory.serialize(user);
		Assertions.assertThat(serialized).isNotEmpty();

		Object deserialized = foryFactory.deserialize(serialized);
		Assertions.assertThat(deserialized).isInstanceOf(User.class);

		User deserializedUser = (User) deserialized;
		Assertions.assertThat(deserializedUser.getId()).isEqualTo(2L);
		Assertions.assertThat(deserializedUser.getName()).isNull();
		Assertions.assertThat(deserializedUser.getAge()).isEqualTo(30);
		Assertions.assertThat(deserializedUser.getEmail()).isNull();
	}

	@Test
	void testSerializeAndDeserializeOrderWithEmptyList() {
		// 测试包含空列表的对象序列化与反序列化
		Order order = new Order("ORD-002", 0.0, new ArrayList<>());
		byte[] serialized = foryFactory.serialize(order);
		Assertions.assertThat(serialized).isNotEmpty();

		Object deserialized = foryFactory.deserialize(serialized);
		Assertions.assertThat(deserialized).isInstanceOf(Order.class);

		Order deserializedOrder = (Order) deserialized;
		Assertions.assertThat(deserializedOrder.getOrderId()).isEqualTo("ORD-002");
		Assertions.assertThat(deserializedOrder.getTotalAmount()).isEqualTo(0.0);
		Assertions.assertThat(deserializedOrder.getItems()).isEmpty();
	}

	/**
	 * 测试用 User 类.
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class User implements Serializable {

		private Long id;

		private String name;

		private Integer age;

		private String email;

	}

	/**
	 * 测试用 Order 类.
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class Order implements Serializable {

		private String orderId;

		private Double totalAmount;

		private List<String> items;

	}

}
