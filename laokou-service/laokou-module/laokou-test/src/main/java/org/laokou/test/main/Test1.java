/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.test.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author laokou
 */
public class Test1 {

    private static Map<String,String> map = new ConcurrentHashMap<>(10000);
    private static Map<String,Boolean> map2 = new ConcurrentHashMap<>(10000);

    private static LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>(256);

    public static void main(String[] args) {
        ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
                9
                , 16
                , 60
                , TimeUnit.SECONDS
                , new LinkedBlockingQueue<>(256)
                , new ThreadPoolExecutor.CallerRunsPolicy());
        List<String> uuids = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            uuids.add(i + "");
        }
        List<CompletableFuture> futures = Collections.synchronizedList(new ArrayList<>(10000));
        for (String uuid : uuids) {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> System.out.println(get(uuid)), EXECUTOR);
            EXECUTOR.execute(() -> test(uuid));
            futures.add(voidCompletableFuture);
        }
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
        EXECUTOR.shutdown();
    }

    public static String get(String uuid) {
        try {
            map.put(uuid,"0");
            synchronized (map) {
                map.wait(1);
            }
            Boolean remove = map2.remove(uuid);
            if (remove != null && remove) {
                return "1";
            }
        } catch (InterruptedException e) {}
        finally {
            map.remove(uuid);
        }
        return "0";
    }

    public static void test(String uuid) {
        String o = map.get(uuid);
        if (o != null && o.length() > 0 && !"".equals(o)) {
            map2.put(uuid,true);
            // 唤醒线程
            synchronized (o) {
                o.notify();
            }
        }
    }
}
