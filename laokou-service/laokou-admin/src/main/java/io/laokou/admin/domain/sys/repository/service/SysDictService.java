package io.laokou.admin.domain.sys.repository.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.admin.domain.sys.entity.SysDictDO;
import io.laokou.admin.interfaces.qo.SysDictQO;
import io.laokou.admin.interfaces.vo.SysDictVO;

import java.util.List;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/6/23 0023 上午 11:04
 */
public interface SysDictService extends IService<SysDictDO> {

    List<SysDictVO> getDictList(SysDictQO qo);

    IPage<SysDictVO> getDictList(IPage<SysDictVO> page, SysDictQO qo);

    SysDictVO getDictById(Long id);

    void deleteDict(Long id);

}
