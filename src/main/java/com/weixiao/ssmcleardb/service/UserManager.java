
package com.weixiao.ssmcleardb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import com.weixiao.frame.base.BaseManager;
import com.weixiao.frame.base.EntityDao;
import com.weixiao.frame.page.Page;
import com.weixiao.frame.page.PageRequest;
import com.weixiao.ssmcleardb.dao.UserDao;
import com.weixiao.ssmcleardb.model.User;

/**
 * @author lipw
 * 表名 user
 * 备注：User
 */
@Component
@Transactional
@SuppressWarnings("rawtypes")
public class UserManager extends BaseManager<User,Long>{

	@Autowired
	private UserDao userDao;
	
	public EntityDao getEntityDao() {
		return this.userDao;
	}

	public Page findByPageRequest(PageRequest pr) {
		return userDao.findByPageRequest(pr);
	}

	public List<User> findBySecond(final Object... params) {
		return userDao.findBySecond(params);
	}
}
