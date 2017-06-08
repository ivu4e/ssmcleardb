package com.weixiao.ssmcleardb.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.weixiao.frame.base.BaseMyIbatisDao;
import com.weixiao.frame.page.Page;
import com.weixiao.frame.page.PageRequest;
import com.weixiao.ssmcleardb.model.User;

/**
 * @author lipw
 * 表名 user
 * 备注：User
 */
@SuppressWarnings("unchecked")
@Component
public class UserDao extends BaseMyIbatisDao<User, Long> {

	@SuppressWarnings("rawtypes")
	public Class getEntityClass() {
		return User.class;
	}
	
	public int saveOrUpdate(User entity) {
		if (entity.getId() == null) { 
			return save(entity);
		} else {
			return update(entity);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public Page findByPageRequest(PageRequest pageRequest) {
		return pageQuery("User.pageSelect", pageRequest);
	}
	
	@SuppressWarnings("rawtypes")
	public List<User> findBySecond(final Object... params) {
		return db().selectList("User.secondSelect", map(params));
	}
}
