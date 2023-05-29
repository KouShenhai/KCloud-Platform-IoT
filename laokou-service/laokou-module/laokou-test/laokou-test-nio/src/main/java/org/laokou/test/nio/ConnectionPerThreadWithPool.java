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

package org.laokou.test.nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
public class ConnectionPerThreadWithPool implements Runnable {

    public static void main(String[] args) {
        EXECUTOR_SERVICE.execute(ConnectionPerThreadWithPool::new);
    }

    private static final ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(8
            ,16
            ,60
            , TimeUnit.SECONDS
            ,new LinkedBlockingQueue<>(256)
            ,new ThreadPoolExecutor.AbortPolicy());
    private static final int PORT = 8088;

    @Override

    public void run() {
        // 服务器监听Socket
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            // 主线程死循环，等待新连接到来
            while (!Thread.interrupted()) {
                Socket socket = serverSocket.accept();
                // 接收一个连接后，为Socket连接新建一个专属的处理器对象
                EXECUTOR_SERVICE.execute(() -> new Handler(socket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static class Handler implements Runnable {

        private final Socket socket;
        Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            // 死循环读写事件
            boolean ioCompleted = false;
            while (!ioCompleted) {
                try {
                    byte[] input = new byte[1024];
                    socket.getInputStream().read(input);
                    ioCompleted = true;
                    System.out.println("读取到的结果：" + new String(input, StandardCharsets.UTF_8));
                } catch (IOException ignored){

                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
