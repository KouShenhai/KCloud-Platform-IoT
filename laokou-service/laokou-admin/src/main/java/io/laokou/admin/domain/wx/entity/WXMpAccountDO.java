package io.laokou.admin.domain.wx.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.laokou.common.entity.BaseDO;
import lombok.Data;
/**
 * 公众号账号管理
 *
 * @author Kou Shenhai
 */
@Data
@TableName("boot_wx_mp_account")
public class WXMpAccountDO extends BaseDO {
	private static final long serialVersionUID = 1L;
	/**
	* 名称
	*/
	private String name;
	/**
	* AppID
	*/
	private String appId;
	/**
	* AppSecret
	*/
	private String appSecret;
	/**
	* Token
	*/
	private String token;
	/**
	* EncodingAESKey
	*/
	private String aesKey;
}