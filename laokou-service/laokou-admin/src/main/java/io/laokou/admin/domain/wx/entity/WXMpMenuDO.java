package io.laokou.admin.domain.wx.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.laokou.common.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 公众号自定义菜单
 *
 * @author Kou Shenhai
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("mp_menu")
public class WXMpMenuDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	/**
	* 菜单json数据
	*/
	private String menu;
	/**
	* AppID
	*/
	private String appId;
}