package io.laokou.admin.interfaces.controller;
import io.laokou.admin.application.service.OssApplicationService;
import io.laokou.admin.interfaces.vo.UploadVO;
import io.laokou.common.exception.CustomException;
import io.laokou.common.utils.HttpResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
/**
 * 对象存储控制器
 * @author Kou Shenhai
 */
@RestController
@Api(value = "对象存储API",protocols = "http",tags = "对象存储API")
@RequestMapping("/oss/api")
public class OssApiController {

    @Autowired
    private OssApplicationService ossApplicationService;

    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("对象存储>上传")
    public HttpResultUtil<UploadVO> upload(@RequestPart("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new CustomException("上传的文件不能为空");
        }
        //文件名
        final String fileName = file.getOriginalFilename();
        //文件流
        final InputStream inputStream = file.getInputStream();
        //文件大小
        final Long fileSize = file.getSize();
        //上传文件
        return new HttpResultUtil<UploadVO>().ok(ossApplicationService.upload(inputStream,fileName,fileSize));
    }

}
