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

package org.laokou.flowable.service.remote;

import org.apache.dubbo.config.annotation.DubboService;
import org.laokou.flowable.api.remote.RemoteTasksServiceI;

/**
 * @author laokou
 */
@DubboService
public class RemoteTaskServiceImpl implements RemoteTasksServiceI {

    @Override
    public void test() {
        System.out.println(1111);
    }

}
