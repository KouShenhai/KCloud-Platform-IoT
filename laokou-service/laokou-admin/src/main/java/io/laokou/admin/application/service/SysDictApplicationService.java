package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.SysDictDTO;
import io.laokou.admin.interfaces.qo.SysDictQO;
import io.laokou.admin.interfaces.vo.SysDictVO;

import javax.servlet.http.HttpServletRequest;

public interface SysDictApplicationService {

    IPage<SysDictVO> queryDictPage(SysDictQO qo);

    SysDictVO getDictById(Long id);

    Boolean insertDict(SysDictDTO dto, HttpServletRequest request);

    Boolean updateDict(SysDictDTO dto, HttpServletRequest request);

    Boolean deleteDict(Long id);

}
