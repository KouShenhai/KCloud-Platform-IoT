package io.laokou.common.utils;
import io.laokou.common.exception.ErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 统一返回结果类
 * @author  Kou Shenhai
 */
@Data
@ApiModel("响应")
public class HttpResultUtil<T> {
    /**
     * 编码：0标识成功，其他值表示失败
     */
    @ApiModelProperty(value = "编码：0标识成功，其他值表示失败",example = "0")
    public int code = 0;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容",example = "success")
    public String msg = "success";


    /**
     * 响应数据
     */
    @ApiModelProperty(value = "响应数据",example = "")
    public T data;

    public boolean success(){
        return code == 0 ? true : false;
    }

    public HttpResultUtil<T> error(){
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = MessageUtil.getMessage(this.code);
        return this;
    }

    public HttpResultUtil<T> error(int code){
        this.code = code;
        this.msg = MessageUtil.getMessage(this.code);
        return this;
    }

    public HttpResultUtil<T> ok(T data){
        this.setData(data);
        return this;
    }

    public HttpResultUtil<T> error(int code,String msg){
        this.code = code;
        this.msg = msg;
        return this;
    }

    public HttpResultUtil<T> error(String msg){
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
        return this;
    }

}
