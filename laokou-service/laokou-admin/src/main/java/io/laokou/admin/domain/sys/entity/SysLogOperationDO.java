package io.laokou.admin.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志 实体类
 */
@Data
@TableName("boot_sys_log_operation")
public class SysLogOperationDO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 模块名称，如：sys
	 */
	private String module;
	/**
	 * 用户操作
	 */
	private String operation;
	/**
	 * 请求URI
	 */
	private String requestUri;
	/**
	 * 请求方式
	 */
	private String requestMethod;
	/**
	 * 请求参数
	 */
	private String requestParams;
	/**
	 * 请求时长(毫秒)
	 */
	private Integer requestTime;
	/**
	 * 用户代理
	 */
	private String userAgent;
	/**
	 * 操作IP
	 */
	private String ip;
	/**
	 * 状态  0：失败   1：成功
	 */
	private Integer status;
	/**
	 * 用户名
	 */
	private String creatorName;
	/**
	 * 创建者
	 */
	private Long creator;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 修改时间
	 */
	private Date updateDate;
}