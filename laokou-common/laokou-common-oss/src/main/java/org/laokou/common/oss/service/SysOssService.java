///*
// * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// */
//package org.laokou.common.oss.service;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.service.IService;
//import org.laokou.common.oss.qo.SysOssQo;
//import org.laokou.common.oss.vo.SysOssVO;
//
///**
// * @author laokou
// */
//public interface SysOssService extends IService<SysOssDO> {
//
//	/**
//	 * 查询OSS配置
//	 * @return
//	 */
//	SysOssVO queryOssConfig();
//
//	/**
//	 * 删除oss
//	 * @param id
//	 * @return
//	 */
//	Boolean deleteOss(Long id);
//
//	/**
//	 * 获取版本号
//	 * @param id
//	 * @return
//	 */
//	Integer getVersion(Long id);
//
//	/**
//	 * 分页查询
//	 * @param page
//	 * @param qo
//	 * @return
//	 */
//	IPage<SysOssVO> queryOssPage(IPage<SysOssVO> page, SysOssQo qo);
//
//	/**
//	 * 查看详情
//	 * @param id
//	 * @return
//	 */
//	SysOssVO getOssById(Long id);
//
//}
