package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.DictDTO;
import io.laokou.admin.interfaces.qo.DictQO;
import io.laokou.admin.interfaces.vo.DictVO;

import javax.servlet.http.HttpServletRequest;

public interface SysDictApplicationService {

    IPage<DictVO> queryDictPage(DictQO qo);

    DictVO getDictById(Long id);

    Boolean insertDict(DictDTO dto, HttpServletRequest request);

    Boolean updateDict(DictDTO dto, HttpServletRequest request);

    Boolean deleteDict(Long id);

}
