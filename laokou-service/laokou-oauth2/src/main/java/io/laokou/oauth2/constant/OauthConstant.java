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
