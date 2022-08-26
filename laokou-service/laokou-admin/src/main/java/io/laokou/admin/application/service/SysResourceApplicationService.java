package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.SysResourceDTO;
import io.laokou.admin.interfaces.qo.SysResourceQO;
import io.laokou.admin.interfaces.vo.SysResourceVO;
import io.laokou.admin.interfaces.vo.UploadVO;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/19 0019 下午 3:42
 */
public interface SysResourceApplicationService {

    IPage<SysResourceVO> queryResourcePage(SysResourceQO qo);

    SysResourceVO getResourceById(Long id);

    Boolean insertResource(SysResourceDTO dto, HttpServletRequest request);

    Boolean updateResource(SysResourceDTO dto, HttpServletRequest request);

    Boolean deleteResource(Long id);

    UploadVO uploadResource(String code,String fileName, InputStream inputStream,Long fileSize) throws Exception;

    Boolean syncAsyncBatchResource(String code);

    void get(String instanceId);

}
