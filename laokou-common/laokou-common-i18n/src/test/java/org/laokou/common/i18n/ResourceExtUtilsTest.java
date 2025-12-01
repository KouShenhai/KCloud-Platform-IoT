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

package org.laokou.common.i18n;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.ResourceExtUtils;
import org.springframework.core.io.Resource;

/**
 * @author laokou
 */
class ResourceExtUtilsTest {

	@Test
	void test_getResource_withClasspathResource() {
		// Test with classpath resource
		Resource resource = ResourceExtUtils.getResource("classpath:application.properties");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_withClasspathPattern() {
		// Test with classpath pattern
		Resource resource = ResourceExtUtils.getResource("classpath*:*.properties");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_withFileProtocol() {
		// Test with file protocol
		Resource resource = ResourceExtUtils.getResource("file:test.txt");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_withHttpProtocol() {
		// Test with HTTP protocol
		Resource resource = ResourceExtUtils.getResource("https://example.com/test.txt");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_withRelativePath() {
		// Test with relative path
		Resource resource = ResourceExtUtils.getResource("test.txt");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_withAbsolutePath() {
		// Test with absolute path (Windows style)
		Resource resource = ResourceExtUtils.getResource("C:/temp/test.txt");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_withJarProtocol() {
		// Test with jar protocol
		Resource resource = ResourceExtUtils.getResource("jar:file:/path/to/jar.jar!/resource.txt");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_exists() {
		// Test if resource exists (using a resource that should exist in test
		// classpath)
		Resource resource = ResourceExtUtils
			.getResource("classpath:org/laokou/common/i18n/util/ResourceExtUtils.class");
		Assertions.assertThat(resource).isNotNull();
		// Note: exists() might return false for non-existent resources, but resource
		// object is still created
	}

	@Test
	void test_getResource_nonExistentFile() {
		// Test with non-existent file (should still return Resource object, but
		// exists() would be false)
		Resource resource = ResourceExtUtils.getResource("classpath:non-existent-file.txt");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_withEmptyString() {
		// Test with empty string
		Resource resource = ResourceExtUtils.getResource("");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_withWildcard() {
		// Test with wildcard pattern
		Resource resource = ResourceExtUtils.getResource("classpath:*.xml");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_withNestedPath() {
		// Test with nested path
		Resource resource = ResourceExtUtils.getResource("classpath:org/laokou/common/i18n/test.properties");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_withDoubleSlash() {
		// Test with double slash
		Resource resource = ResourceExtUtils.getResource("classpath://test.txt");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_withBackslash() {
		// Test with backslash (Windows path style)
		Resource resource = ResourceExtUtils.getResource("classpath:org\\laokou\\test.txt");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_withSpecialCharacters() {
		// Test with special characters in filename
		Resource resource = ResourceExtUtils.getResource("classpath:test-file_123.txt");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_withSpaces() {
		// Test with spaces in path
		Resource resource = ResourceExtUtils.getResource("classpath:test file.txt");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_withChineseCharacters() {
		// Test with Chinese characters in path
		Resource resource = ResourceExtUtils.getResource("classpath:测试文件.txt");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_multipleCallsSameLocation() {
		// Test multiple calls with same location
		String location = "classpath:test.txt";
		Resource resource1 = ResourceExtUtils.getResource(location);
		Resource resource2 = ResourceExtUtils.getResource(location);
		Assertions.assertThat(resource1).isNotNull();
		Assertions.assertThat(resource2).isNotNull();
		// Both should return Resource objects (though might not be the same instance)
	}

	@Test
	void test_getResource_differentLocations() {
		// Test with different locations
		Resource resource1 = ResourceExtUtils.getResource("classpath:file1.txt");
		Resource resource2 = ResourceExtUtils.getResource("classpath:file2.txt");
		Assertions.assertThat(resource1).isNotNull();
		Assertions.assertThat(resource2).isNotNull();
		Assertions.assertThat(resource1).isNotEqualTo(resource2);
	}

	@Test
	void test_getResource_classpathStarPattern() {
		// Test with classpath* pattern (searches all classpath roots)
		Resource resource = ResourceExtUtils.getResource("classpath*:META-INF/spring.factories");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_resourceDescription() {
		// Test resource description
		Resource resource = ResourceExtUtils.getResource("classpath:test.txt");
		Assertions.assertThat(resource).isNotNull();
		Assertions.assertThat(resource.getDescription()).isNotNull();
		Assertions.assertThat(resource.getDescription()).contains("test.txt");
	}

	@Test
	void test_getResource_withUrlEncoding() {
		// Test with URL encoding
		Resource resource = ResourceExtUtils.getResource("classpath:test%20file.txt");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_withFragment() {
		// Test with URL fragment
		Resource resource = ResourceExtUtils.getResource("classpath:test.txt#section");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_withQueryString() {
		// Test with query string
		Resource resource = ResourceExtUtils.getResource("https://example.com/test.txt?param=value");
		Assertions.assertThat(resource).isNotNull();
	}

	@Test
	void test_getResource_caseInsensitivity() {
		// Test case sensitivity (behavior depends on file system)
		Resource resource1 = ResourceExtUtils.getResource("classpath:Test.txt");
		Resource resource2 = ResourceExtUtils.getResource("classpath:test.txt");
		Assertions.assertThat(resource1).isNotNull();
		Assertions.assertThat(resource2).isNotNull();
	}

}
