package io.laokou.admin.application.service.impl;
import io.laokou.admin.application.service.WXMpAccountApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * 公众号账号管理
 * @author limingze
 *  * @create: 2022-07-12 18:03
 */
@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
public class WXMpAccountApplicationServiceImpl implements WXMpAccountApplicationService {

}