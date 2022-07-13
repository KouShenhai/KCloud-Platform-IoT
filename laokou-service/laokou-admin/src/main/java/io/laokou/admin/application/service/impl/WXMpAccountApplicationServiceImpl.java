package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.application.service.WXMpAccountApplicationService;
import io.laokou.admin.domain.wx.entity.WXMpAccountDO;
import io.laokou.admin.domain.wx.repository.dao.WXMpAccountDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 公众号账号管理
 * @author limingze
 *  * @create: 2022-07-12 18:03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WXMpAccountApplicationServiceImpl extends ServiceImpl<WXMpAccountDao,WXMpAccountDO> implements WXMpAccountApplicationService {

}