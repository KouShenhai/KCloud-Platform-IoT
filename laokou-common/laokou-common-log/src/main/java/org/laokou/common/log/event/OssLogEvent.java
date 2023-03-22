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

package org.laokou.common.log.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;
import java.io.Serializable;
import java.time.Clock;

/**
 * @author laokou
 */
@Getter
@Setter
public class OssLogEvent extends ApplicationEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 3776732013732856552L;

    private String md5;
    private String url;
    private String fileName;
    private Long fileSize;

    public OssLogEvent(Object source) {
        super(source);
    }

    public OssLogEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
