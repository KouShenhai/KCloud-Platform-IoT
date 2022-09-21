/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package io.laokou.oauth2.constant;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/10 0010 下午 4:28
 */
public interface OauthConstant {

   String SELECT_STATEMENT = "select client_id,client_secret,resource_ids, scope,authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity,refresh_token_validity, additional_information, autoapprove from boot_sys_oauth_client_details where client_id = ?";

   String FIND_STATEMENT = "select client_id,client_secret,resource_ids, scope,authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity,refresh_token_validity, additional_information, autoapprove from boot_sys_oauth_client_details order by client_id";

}
