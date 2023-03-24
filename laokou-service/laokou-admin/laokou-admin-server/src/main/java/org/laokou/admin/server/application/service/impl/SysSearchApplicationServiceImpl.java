///**
// * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
// * <p>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * <p>
// *   http://www.apache.org/licenses/LICENSE-2.0
// * <p>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package org.laokou.admin.server.application.service.impl;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.laokou.admin.server.application.service.SysSearchApplicationService;
//import org.laokou.common.i18n.core.CustomException;
//import org.laokou.common.i18n.utils.ValidatorUtil;
//import org.springframework.stereotype.Service;
//import java.util.Map;
///**
// * @author laokou
// */
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class SysSearchApplicationServiceImpl implements SysSearchApplicationService {
//    private final ElasticsearchApiFeignClient elasticsearchApiFeignClient;
//
//    @Override
//    public SearchVO<Map<String,Object>> searchResource(SearchQo qo) {
//        ValidatorUtil.validateEntity(qo);
//        HttpResult<SearchVO<Map<String, Object>>> result = elasticsearchApiFeignClient.highlightSearch(qo);
//        if (!result.success()) {
//            throw new CustomException(result.getCode(),result.getMsg());
//        }
//        return result.getData();
//    }
//}
