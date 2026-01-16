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

package org.laokou.common.lock;

import com.redis.testcontainers.RedisContainer;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.redis.config.JacksonCodec;
import org.laokou.common.redis.util.RedisUtils;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RedissonLock test class using Testcontainers for integration testing.
 *
 * @author laokou
 */
@Testcontainers
class RedissonLockTest {

	@Container
	static RedisContainer redisContainer = new RedisContainer(DockerImageNames.redis()).withExposedPorts(6379)
		.withReuse(true);

	private RedissonLock redissonLock;

	private RedisUtils redisUtils;

	@BeforeEach
	void setUp() {
		String redisHost = redisContainer.getHost();
		Integer redisPort = redisContainer.getMappedPort(6379);

		// Configure Lettuce Connection Factory
		RedisTemplate<String, Object> redisTemplate = getStringObjectRedisTemplate(redisHost, redisPort);
		redisTemplate.afterPropertiesSet();

		// Configure Redisson
		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);
		config.setCodec(JacksonCodec.INSTANCE);
		RedissonClient redissonClient = Redisson.create(config);
		redisUtils = new RedisUtils(redisTemplate, redissonClient);
		redissonLock = new RedissonLock(redisUtils);
	}

	// ==================== Basic Lock Operations ====================

	@Test
	@DisplayName("Test basic lock acquisition and release")
	void testBasicLockAndUnlock() throws InterruptedException {
		String key = "test:lock:basic";

		boolean acquired = redissonLock.tryLock(Type.LOCK, key, 1000);
		Assertions.assertThat(acquired).isTrue();

		RLock lock = redissonLock.getLock(Type.LOCK, key);
		Assertions.assertThat(redisUtils.isLocked(lock)).isTrue();
		Assertions.assertThat(redisUtils.isHeldByCurrentThread(lock)).isTrue();

		redissonLock.unlock(Type.LOCK, key);
		Assertions.assertThat(redisUtils.isLocked(lock)).isFalse();
	}

	@Test
	@DisplayName("Test fair lock acquisition and release")
	void testFairLockAndUnlock() throws InterruptedException {
		String key = "test:lock:fair";

		boolean acquired = redissonLock.tryLock(Type.FAIR_LOCK, key, 1000);
		Assertions.assertThat(acquired).isTrue();

		RLock lock = redissonLock.getLock(Type.FAIR_LOCK, key);
		Assertions.assertThat(redisUtils.isLocked(lock)).isTrue();

		redissonLock.unlock(Type.FAIR_LOCK, key);
		Assertions.assertThat(redisUtils.isLocked(lock)).isFalse();
	}

	@Test
	@DisplayName("Test read lock acquisition and release")
	void testReadLockAndUnlock() throws InterruptedException {
		String key = "test:lock:read";

		boolean acquired = redissonLock.tryLock(Type.READ_LOCK, key, 1000);
		Assertions.assertThat(acquired).isTrue();

		RLock lock = redissonLock.getLock(Type.READ_LOCK, key);
		Assertions.assertThat(redisUtils.isLocked(lock)).isTrue();

		redissonLock.unlock(Type.READ_LOCK, key);
		Assertions.assertThat(redisUtils.isLocked(lock)).isFalse();
	}

	@Test
	@DisplayName("Test write lock acquisition and release")
	void testWriteLockAndUnlock() throws InterruptedException {
		String key = "test:lock:write";

		boolean acquired = redissonLock.tryLock(Type.WRITE_LOCK, key, 1000);
		Assertions.assertThat(acquired).isTrue();

		RLock lock = redissonLock.getLock(Type.WRITE_LOCK, key);
		Assertions.assertThat(redisUtils.isLocked(lock)).isTrue();

		redissonLock.unlock(Type.WRITE_LOCK, key);
		Assertions.assertThat(redisUtils.isLocked(lock)).isFalse();
	}

	@Test
	@DisplayName("Test fenced lock acquisition and release")
	void testFencedLockAndUnlock() throws InterruptedException {
		String key = "test:lock:fenced";

		boolean acquired = redissonLock.tryLock(Type.FENCED_LOCK, key, 1000);
		Assertions.assertThat(acquired).isTrue();

		RLock lock = redissonLock.getLock(Type.FENCED_LOCK, key);
		Assertions.assertThat(redisUtils.isLocked(lock)).isTrue();

		redissonLock.unlock(Type.FENCED_LOCK, key);
		Assertions.assertThat(redisUtils.isLocked(lock)).isFalse();
	}

	// ==================== Lock Timeout Tests ====================

	@Test
	@DisplayName("Test lock timeout when lock is held by another thread")
	void testLockTimeout() throws InterruptedException {
		String key = "test:lock:timeout";

		// Acquire lock in main thread
		boolean acquired = redissonLock.tryLock(Type.LOCK, key, 1000);
		Assertions.assertThat(acquired).isTrue();

		// Try to acquire same lock in another thread with short timeout
		AtomicInteger failedAttempts = new AtomicInteger(0);
		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
			try {
				RedissonLock anotherLock = new RedissonLock(redisUtils);
				boolean result = anotherLock.tryLock(Type.LOCK, key, 100);
				if (!result) {
					failedAttempts.incrementAndGet();
				}
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});

		future.join();
		Assertions.assertThat(failedAttempts.get()).isEqualTo(1);

		redissonLock.unlock(Type.LOCK, key);
	}

	// ==================== Concurrent Lock Tests ====================

	@Test
	@DisplayName("Test concurrent lock ensures mutual exclusion")
	void testConcurrentLockMutualExclusion() throws InterruptedException {
		String key = "test:lock:concurrent";
		int threadCount = 5;
		AtomicInteger counter = new AtomicInteger(0);
		AtomicInteger successCount = new AtomicInteger(0);

		try (ExecutorService executorService = ThreadUtils.newVirtualTaskExecutor()) {
			List<Callable<Boolean>> futures = new ArrayList<>(threadCount);
			for (int i = 0; i < threadCount; i++) {
				futures.add(() -> {
					try {
						RedissonLock lock = new RedissonLock(redisUtils);
						if (lock.tryLock(Type.LOCK, key, 5000)) {
							try {
								successCount.incrementAndGet();
								// Simulate critical section
								int value = counter.get();
								Thread.sleep(50);
								counter.set(value + 1);
							}
							finally {
								lock.unlock(Type.LOCK, key);
							}
						}
					}
					catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					return true;
				});
			}
			executorService.invokeAll(futures);
			// All threads should have succeeded with proper locking
			Assertions.assertThat(successCount.get()).isEqualTo(threadCount);
			// Counter should equal thread count due to mutual exclusion
			Assertions.assertThat(counter.get()).isEqualTo(threadCount);
		}
	}

	@Test
	@DisplayName("Test multiple read locks can be acquired concurrently")
	void testMultipleReadLocks() throws InterruptedException {
		String key = "test:lock:multiread";
		int threadCount = 3;
		AtomicInteger acquiredCount = new AtomicInteger(0);
		try (ExecutorService executorService = ThreadUtils.newVirtualTaskExecutor()) {
			List<Callable<Boolean>> futures = new ArrayList<>(threadCount);
			for (int i = 0; i < threadCount; i++) {
				futures.add(() -> {
					try {
						RedissonLock lock = new RedissonLock(redisUtils);
						if (lock.tryLock(Type.READ_LOCK, key, 5000)) {
							acquiredCount.incrementAndGet();
							Thread.sleep(100);
							lock.unlock(Type.READ_LOCK, key);
						}
					}
					catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					return true;
				});
			}
			executorService.invokeAll(futures);
			// All read locks should have been acquired
			Assertions.assertThat(acquiredCount.get()).isEqualTo(threadCount);
		}
	}

	@Test
	@DisplayName("Test write lock blocks read locks")
	void testWriteLockBlocksReadLock() throws InterruptedException {
		String key = "test:lock:writeblock";

		// Acquire write lock first
		boolean writeAcquired = redissonLock.tryLock(Type.WRITE_LOCK, key, 1000);
		Assertions.assertThat(writeAcquired).isTrue();

		// Try to acquire read lock in another thread
		AtomicInteger readAttempts = new AtomicInteger(0);
		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
			try {
				RedissonLock anotherLock = new RedissonLock(redisUtils);
				boolean readResult = anotherLock.tryLock(Type.READ_LOCK, key, 100);
				if (!readResult) {
					readAttempts.incrementAndGet();
				}
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});

		future.join();

		// Read lock should have failed while write lock is held
		Assertions.assertThat(readAttempts.get()).isEqualTo(1);

		redissonLock.unlock(Type.WRITE_LOCK, key);
	}

	// ==================== Edge Cases ====================

	@Test
	@DisplayName("Test unlock without holding lock does not throw exception")
	void testUnlockWithoutHoldingLock() {
		String key = "test:lock:notheld";

		// Should not throw exception
		Assertions.assertThatCode(() -> redissonLock.unlock(Type.LOCK, key)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Test getLock returns valid lock object")
	void testGetLockReturnsValidLock() {
		String key = "test:lock:getlock";

		RLock lock = redissonLock.getLock(Type.LOCK, key);
		Assertions.assertThat(lock).isNotNull();
		Assertions.assertThat(lock.getName()).contains(key);

		RLock fairLock = redissonLock.getLock(Type.FAIR_LOCK, key);
		Assertions.assertThat(fairLock).isNotNull();

		RLock readLock = redissonLock.getLock(Type.READ_LOCK, key);
		Assertions.assertThat(readLock).isNotNull();

		RLock writeLock = redissonLock.getLock(Type.WRITE_LOCK, key);
		Assertions.assertThat(writeLock).isNotNull();

		RLock fencedLock = redissonLock.getLock(Type.FENCED_LOCK, key);
		Assertions.assertThat(fencedLock).isNotNull();
	}

	@Test
	@DisplayName("Test reentrant lock allows same thread to acquire multiple times")
	void testReentrantLock() throws InterruptedException {
		String key = "test:lock:reentrant";

		// First acquisition
		boolean firstAcquired = redissonLock.tryLock(Type.LOCK, key, 1000);
		Assertions.assertThat(firstAcquired).isTrue();

		// Second acquisition (reentrant)
		boolean secondAcquired = redissonLock.tryLock(Type.LOCK, key, 1000);
		Assertions.assertThat(secondAcquired).isTrue();

		RLock lock = redissonLock.getLock(Type.LOCK, key);
		Assertions.assertThat(lock.getHoldCount()).isEqualTo(2);

		// Need to unlock twice
		redissonLock.unlock(Type.LOCK, key);
		Assertions.assertThat(lock.getHoldCount()).isEqualTo(1);
		Assertions.assertThat(redisUtils.isLocked(lock)).isTrue();

		redissonLock.unlock(Type.LOCK, key);
		Assertions.assertThat(redisUtils.isLocked(lock)).isFalse();
	}

	@NotNull
	private RedisTemplate<String, Object> getStringObjectRedisTemplate(String redisHost, Integer redisPort) {
		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisConfig);
		connectionFactory.afterPropertiesSet();

		// Configure RedisTemplate
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		GenericJacksonJsonRedisSerializer genericJacksonJsonRedisSerializer = new GenericJacksonJsonRedisSerializer(
				new JsonMapper());
		redisTemplate.setValueSerializer(genericJacksonJsonRedisSerializer);
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(genericJacksonJsonRedisSerializer);
		return redisTemplate;
	}

}
