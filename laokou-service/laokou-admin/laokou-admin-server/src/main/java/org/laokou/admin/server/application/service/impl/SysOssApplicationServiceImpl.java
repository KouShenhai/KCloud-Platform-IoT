/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.server.application.service.impl;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.laokou.admin.client.dto.SysOssDTO;
import org.laokou.admin.server.application.service.SysOssApplicationService;
import org.laokou.admin.server.application.service.SysResourceApplicationService;
import org.laokou.common.oss.entity.SysOssDO;
import org.laokou.common.oss.qo.SysOssQo;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.common.oss.service.SysOssService;
import org.laokou.common.oss.vo.SysOssVO;
import org.laokou.common.oss.vo.UploadVO;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class SysOssApplicationServiceImpl implements SysOssApplicationService {

    private final SysOssService sysOssService;
    private final SysResourceApplicationService sysResourceApplicationService;
    private final RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DS(Constant.TENANT)
    public Boolean insertOss(SysOssDTO dto) {
        ValidatorUtil.validateEntity(dto);
        long count = sysOssService.count(Wrappers.lambdaQuery(SysOssDO.class).eq(SysOssDO::getName, dto.getName()));
        if (count > 0) {
            throw new CustomException("存储名称已存在，请重新填写");
        }
        SysOssDO sysOssDO = ConvertUtil.sourceToTarget(dto, SysOssDO.class);
        sysOssDO.setCreator(UserUtil.getUserId());
        return sysOssService.save(sysOssDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DS(Constant.TENANT)
    public Boolean updateOss(SysOssDTO dto) {
        ValidatorUtil.validateEntity(dto);
        Long id = dto.getId();
        if (id == null) {
            throw new CustomException("存储编号不为空");
        }
        long useCount = sysOssService.count(Wrappers.lambdaQuery(SysOssDO.class).eq(SysOssDO::getStatus, Constant.YES).eq(SysOssDO::getId,id));
        if (useCount > 0) {
            throw new CustomException("该配置正在使用，请修改其他配置");
        }
        long count = sysOssService.count(Wrappers.lambdaQuery(SysOssDO.class).eq(SysOssDO::getName, dto.getName()).ne(SysOssDO::getId,id));
        if (count > 0) {
            throw new CustomException("存储名称已存在，请重新填写");
        }
        Integer version = sysOssService.getVersion(id);
        SysOssDO sysOssDO = ConvertUtil.sourceToTarget(dto, SysOssDO.class);
        sysOssDO.setVersion(version);
        sysOssDO.setEditor(UserUtil.getUserId());
        return sysOssService.updateById(sysOssDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DS(Constant.TENANT)
    public Boolean deleteOss(Long id) {
        return sysOssService.deleteOss(id);
    }

    @Override
    @DS(Constant.TENANT)
    public IPage<SysOssVO> queryOssPage(SysOssQo qo) {
        ValidatorUtil.validateEntity(qo);
        IPage<SysOssVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysOssService.queryOssPage(page,qo);
    }

    @Override
    @DS(Constant.TENANT)
    public SysOssVO getOssById(Long id) {
        return sysOssService.getOssById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DS(Constant.TENANT)
    public Boolean useOss(Long id) {
        LambdaQueryWrapper<SysOssDO> wrapper = Wrappers.lambdaQuery(SysOssDO.class)
                .and(t -> t.eq(SysOssDO::getStatus, Constant.YES)
                        .or()
                        .eq(SysOssDO::getId, id));
        List<SysOssDO> list = sysOssService.list(wrapper);
        list.forEach(item -> {
            if (id.equals(item.getId())) {
                item.setStatus(Constant.YES);
            } else {
                item.setStatus(Constant.NO);
            }
        });
        sysOssService.updateBatchById(list);
        String ossConfigKey = RedisKeyUtil.getOssConfigKey(UserUtil.getTenantId());
        redisUtil.delete(ossConfigKey);
        return true;
    }

    @Override
    @SneakyThrows
    public UploadVO upload(MultipartFile file,String md5) {
        return sysResourceApplicationService.uploadResource(null,file,md5);
    }
}
