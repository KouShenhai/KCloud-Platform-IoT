package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.qo.DictQO;
import io.laokou.admin.interfaces.vo.DictVO;

public interface SysDictApplicationService {

    IPage<DictVO> queryDictPage(DictQO qo);

}
