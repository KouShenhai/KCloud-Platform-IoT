package io.laokou.datasource.config;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import io.laokou.common.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Objects;
/**
 * @author Kou Shenhai
 */
@Component
@Slf4j
public class BaseDetaObjectHander implements MetaObjectHandler {

    private static final String CREATE_DATE = "createDate";
    private static final String UPDATE_DATE = "updateDate";
    private static final String DEL_FLAG = "delFlag";

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("come to insert fill .........");
        if (Objects.isNull(getFieldValByName(CREATE_DATE,metaObject))) {
            this.setFieldValByName(CREATE_DATE,new Date(),metaObject);
        }
        if (Objects.isNull(getFieldValByName(UPDATE_DATE,metaObject))) {
            this.setFieldValByName(UPDATE_DATE,new Date(),metaObject);
        }
        if (Objects.isNull(getFieldValByName(DEL_FLAG,metaObject))) {
            this.setFieldValByName(DEL_FLAG, Constant.NO, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("come to update fill .......");
        this.setFieldValByName(UPDATE_DATE,new Date(),metaObject);
        this.setFieldValByName(DEL_FLAG, Constant.NO, metaObject);
    }

}
