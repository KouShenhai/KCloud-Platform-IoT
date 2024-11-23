package org.laokou.auth.convertor;

import org.laokou.auth.dto.clientobject.TenantCO;
import org.laokou.auth.gatewayimpl.database.dataobject.TenantDO;

public class TenantConvertor {

	public static TenantCO toClientObject(TenantDO tenantDO) {
		TenantCO tenantCO = new TenantCO();
  		tenantCO.setId(tenantDO.getId());
  		tenantCO.setName(tenantDO.getName());
		  return tenantCO;
	}

}
