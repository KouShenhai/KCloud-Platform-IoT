package org.laokou.admin.service;

import org.laokou.admin.client.api.UsersServiceI;
import org.laokou.admin.client.dto.UserInsertCmd;
import org.laokou.admin.client.dto.UserUpdateCmd;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Service
public class UsersServiceImpl implements UsersServiceI {

	@Override
	public Boolean update(UserUpdateCmd cmd) {
		return null;
	}

	@Override
	public Boolean insert(UserInsertCmd cmd) {
		return null;
	}

}
