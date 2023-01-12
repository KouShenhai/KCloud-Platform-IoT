/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.mybatisplus.utils;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.laokou.common.mybatisplus.mapper.BaseBatchDao;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MapperUtil<T> {

    private final SqlSessionFactory sqlSessionFactory;

    public void insertBatch(List<T> dataList, int batchNum, BaseBatchDao<T> baseBatchDao){
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        List<List<T>> partition = Lists.partition(dataList, batchNum);
        for(int i = 0; i < partition.size(); i++) {
            try {
                baseBatchDao.insertBatch(partition.get(i));
            } catch (Exception e) {
                sqlSession.rollback();
                log.error("错误信息：{}",e.getMessage());
            }
            if (i % batchNum == 0) {
                sqlSession.commit();
                sqlSession.clearCache();
            }
        }
        sqlSession.commit();
        sqlSession.clearCache();
        sqlSession.close();
    }

}
