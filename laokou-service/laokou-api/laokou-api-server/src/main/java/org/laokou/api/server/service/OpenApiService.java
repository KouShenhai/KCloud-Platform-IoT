package org.laokou.api.server.service;

import org.laokou.api.client.ParamDTO;
import reactor.core.publisher.Mono;
/**
 * @author laokou
 */
public interface OpenApiService {

    /**
     * get请求
     * @param uri
     * @return
     */
   Mono<String> doGet(String uri);

    /**
     * post请求
     * @return
     * @param dto
     */
    Mono<String> doPost(ParamDTO dto);

    /**
     * 转成post请求
     * @param dto
     * @return
     */
    Mono<String> toPost(ParamDTO dto);

}
