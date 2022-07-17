package io.laokou.admin.infrastructure.common.cloud;
import com.alibaba.fastjson.JSON;
import io.laokou.admin.infrastructure.component.AdminHandler;
import io.laokou.admin.infrastructure.config.CloudStorageConfig;
import io.laokou.admin.infrastructure.common.enums.CloudTypeEnum;
import io.laokou.common.exception.CustomException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
/**
 * @author Kou Shenhai
 */
@Component
@AllArgsConstructor
public class CloudFactory {

   private final AdminHandler adminHandler;

   public  AbstractCloudStorageService build(){
       String oss = adminHandler.getOss();
       if (StringUtils.isBlank(oss)){
           throw new CustomException("请配置OSS");
       }
       CloudStorageConfig config = JSON.parseObject(oss, CloudStorageConfig.class);
       if (CloudTypeEnum.ALIYUN.ordinal() == config.getType()){
           return new AliyunCloudStorageService(config);
       }
       else if (CloudTypeEnum.LOCAL.ordinal() == config.getType()){
           return new LocalCloudStorageService(config);
       }
       else if (CloudTypeEnum.FASTDFS.ordinal() == config.getType()){
           return new FastDFSCloudStorageService(config);
       }
       throw new CustomException("请检查OSS相关配置");
   }

}
