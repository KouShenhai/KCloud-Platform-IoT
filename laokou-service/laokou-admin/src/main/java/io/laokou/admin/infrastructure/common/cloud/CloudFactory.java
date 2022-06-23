package io.laokou.admin.infrastructure.common.cloud;
import com.alibaba.fastjson.JSON;
import io.laokou.admin.domain.sys.repository.service.SysDictService;
import io.laokou.admin.infrastructure.config.CloudStorageConfig;
import io.laokou.admin.interfaces.qo.DictQO;
import io.laokou.admin.interfaces.vo.DictVO;
import io.laokou.common.enums.CloudTypeEnum;
import io.laokou.common.exception.CustomException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
/**
 * @author Kou Shenhai
 */
@Component
public class CloudFactory {

   @Autowired
   private SysDictService sysDictService;

   public  AbstractCloudStorageService build(){
       DictQO qo = new DictQO();
       qo.setType("oss");
       List<DictVO> dictList = sysDictService.getDictList(qo);
       if (CollectionUtils.isEmpty(dictList)){
           throw new CustomException("请在系统字典配置对象存储");
       }
       CloudStorageConfig config = JSON.parseObject(dictList.get(0).getDictValue(), CloudStorageConfig.class);
       if (CloudTypeEnum.ALIYUN.ordinal() == config.getType()){
           return new AliyunCloudStorageService(config);
       }
       else if (CloudTypeEnum.LOCAL.ordinal() == config.getType()){
           return new LocalCloudStorageService(config);
       }
       else if (CloudTypeEnum.FASTDFS.ordinal() == config.getType()){
           return new FastDFSCloudStorageService(config);
       }
       throw new CustomException("请检查对象存储相关配置");
   }

}
