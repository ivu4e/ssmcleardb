package com.weixiao.frame.base;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.util.Assert;

import com.weixiao.frame.page.Page;
import com.weixiao.frame.page.PageRequest;

/**
 * @author lipw
 * @version 1.0
 */
public abstract class BaseMyIbatisDao<E, PK extends Serializable> extends
		SqlSessionDaoSupport implements EntityDao<E, PK> {
	protected final Log log = LogFactory.getLog(getClass());

	public abstract Class getEntityClass();

	@Resource  
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){  
        super.setSqlSessionFactory(sqlSessionFactory);  
    }  
	
	public SqlSessionTemplate db() {
		return (SqlSessionTemplate) getSqlSession();
	}

	public E getById(PK primaryKey) {
		final Object object = db().selectOne(getFindByPrimaryKeyStatement(),
				primaryKey);
		return (E) object;
	}

	public List<E> getByIds(String ids) {
		if (StringUtils.isNotBlank(ids)) {
			if (ids.endsWith(",")) {
				ids = ids.substring(0, ids.length() - 1);
			}
			String[] id = ids.split(",");
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < id.length; i++) {
				list.add(id[i]);
			}
			return db().selectList(
					getEntityClass().getSimpleName() + ".getByIds", list);
		}
		return new ArrayList<E>();
	}
	
	public List<E> getByIds(List<Long> list) {
		if (list == null || list.isEmpty()) {
			return new ArrayList<E>();
		}
		return db().selectList(
				getEntityClass().getSimpleName() + ".getByIds", list);
	}

	public void deleteById(PK id) {
		deleteBy("id", id);
	}

	public void deleteBy(final Object... params) {
		db().delete(getDeleteStatement(), map(params));
	}

	/**
	 * 根据Id集合批量删除
	 * 
	 * @param ids
	 */
	public void deleteByIds(String id) {
		String sqlName = getEntityClass().getSimpleName() + ".batchDelete";
		if (StringUtils.isNotBlank(id)) {
			if (id.endsWith(",")) {
				id = id.substring(0, id.length() - 1);
			}
			String[] ids = id.split(",");
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < ids.length; i++) {
				list.add(ids[i]);
			}
			db().delete(sqlName, list);
		}
	}
	

	/**
	 * 根据id集合 批量删除
	 * 
	 * @param list
	 */
	public void deleteByIds(List<Long> list) {
		if (list == null || list.isEmpty())
			return;
		db().delete(getEntityClass().getSimpleName() + ".batchDelete", list);
	}
	
	/**
	 * 根据localId集合批量删除
	 * 
	 * @param ids
	 */
	public void deleteByLocalIds(String id) {
		String sqlName = getEntityClass().getSimpleName() + ".batchDeleteByLocalId";
		if (StringUtils.isNotBlank(id)) {
			if (id.endsWith(",")) {
				id = id.substring(0, id.length() - 1);
			}
			String[] ids = id.split(",");
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < ids.length; i++) {
				list.add(ids[i]);
			}
			db().delete(sqlName, list);
		}
	}
	

	/**
	 * 根据localid集合 批量删除
	 * 
	 * @param list
	 */
	public void deleteByLocalIds(List<String> list) {
		if (list == null || list.isEmpty())
			return;
		db().delete(getEntityClass().getSimpleName() + ".batchDeleteByLocalId", list);
	}

	/**
	 * 删除对象
	 * 
	 * @param entity
	 */
	public void remove(BaseEntity entity) {
		if (entity == null || entity.getId() == null)
			return;
		db().delete(getDeleteStatement(), map("id", entity.getId()));
	}

	/**
	 * 删除对象集合
	 * 
	 * @param list
	 */
	void remove(List<BaseEntity> list) {
		if (list == null || list.isEmpty())
			return;
		List<Long> idList = new ArrayList<Long>();
		for (BaseEntity obj : list) {
			if (obj.getId() != null) {
				idList.add(obj.getId());
			}
		}
		db().delete(getEntityClass().getSimpleName() + ".batchDelete", idList);
	}

	/**
	 * 根据id集合 批量修改
	 */
	public void updateByObjects(String id) {
		String sqlName = getEntityClass().getSimpleName() + ".batchUpdate";
		if (StringUtils.isNotBlank(id)) {
			if (id.endsWith(",")) {
				id = id.substring(0, id.length() - 1);
			}
			String[] ids = id.split(",");
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < ids.length; i++) {
				list.add(ids[i]);
			}
			db().update(sqlName, list);
		}
	}
	
	public void updateByIds(List<Long> list) {
		db().update(getEntityClass().getSimpleName() + ".batchUpdate", list);
	}

	public int save(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		return db().insert(getInsertStatement(), entity);
	}

	public int update(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		int affectCount = db().update(getUpdateStatement(), entity);
		return affectCount;
	}
	
	public int updateByLocalId(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		int affectCount = db().update(getEntityClass().getSimpleName() + ".updateByLocalId", entity);
		return affectCount;
	}

	/**
	 * 用于子类覆盖,在insert,update之前调用.
	 */
	protected void prepareObjectForSaveOrUpdate(final E o) {
	}

	public String getFindByPrimaryKeyStatement() {
		return getEntityClass().getSimpleName() + ".getById";
	}

	public String getInsertStatement() {
		return getInsertStatement(getEntityClass());
	}

	private String getInsertStatement(final Class clazz) {
		return clazz.getSimpleName() + ".insert";
	}

	public String getUpdateStatement() {
		return getUpdateStatement(getEntityClass());
	}

	private String getUpdateStatement(final Class clazz) {
		return clazz.getSimpleName() + ".update";
	}

	public String getDeleteStatement() {
		return getDeleteStatement(getEntityClass());
	}

	private String getDeleteStatement(final Class clazz) {
		return clazz.getSimpleName() + ".delete";
	}

	public String getCountQuery() {
		return getEntityClass().getSimpleName() + ".count";
	}

	public String getPageSelect() {
		return getEntityClass().getSimpleName() + ".pageSelect";
	}

	public int count(final Object... params) {
		final Map map = map(params);
		final Number count = (Number) this.db().selectOne(getCountQuery(), map);
		return count.intValue();
	}

	/**
	 * 分页查询 如果sql中分页条件必须是原生map.xml生成
	 * 
	 * @param statementName
	 *            map.xml对应的sql名字
	 * @param pageRequest
	 *            分页请求
	 * @return
	 */
	public Page pageQuery(String statementName, PageRequest pageRequest) {
		Number totalCount = null;
		try {
			totalCount = (Number) db().selectOne(getCountQuery(),
					PropertyUtils.describe(pageRequest).get("filters"));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (totalCount == null || totalCount.longValue() <= 0) {
			return new Page(pageRequest, 0);
		}

		Page page = new Page(pageRequest, totalCount.intValue());

		// 其它分页参数,用于不喜欢或是因为兼容性而不使用方言(Dialect)的分页用户使用.
		// 与db().queryForList(statementName, parameterObject)配合使用
		Map filters = new HashMap();
		filters.put("offset", page.getFirstResult());
		filters.put("pageSize", page.getPageSize());
		filters.put("lastRows", page.getFirstResult() + page.getPageSize());
		filters.put("sortColumns", pageRequest.getSortColumns());

		Map parameterObject = null;
		try {
			parameterObject = PropertyUtils.describe(pageRequest);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Object filtersPram = parameterObject.get("filters");
        if(filtersPram != null){
            filters.putAll((Map) filtersPram);
        }

		RowBounds rowBounds = new RowBounds(page.getFirstResult(),
				page.getPageSize());
		List list = db().selectList(statementName, filters, rowBounds);
		page.setResult(list);
		return page;
	}

	/**
	 * 分页查询 如果sql中分页条件不是原生map.xml生成的话
	 * 
	 * @param statementName
	 *            map.xml对应的sql名字
	 * @param pageRequest
	 *            分页请求
	 * @param isUseSelfSqlCount
	 *            是否使用自身的sql统计,需要在map.xml中配置statementName+"Count"的sql统计
	 * @return
	 */
	public Page pageQuery(String statementName, PageRequest pageRequest,
			boolean isUseSelfSqlCount) {
		if (!isUseSelfSqlCount) {
			return this.pageQuery(statementName, pageRequest);
		}
		Number totalCount = null;
		try {
			totalCount = (Number) db().selectOne(statementName + "Count",
					PropertyUtils.describe(pageRequest).get("filters"));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (totalCount == null || totalCount.longValue() <= 0) {
			return new Page(pageRequest, 0);
		}

		Page page = new Page(pageRequest, totalCount.intValue());

		// 其它分页参数,用于不喜欢或是因为兼容性而不使用方言(Dialect)的分页用户使用.
		// 与db().queryForList(statementName, parameterObject)配合使用
		Map filters = new HashMap();
		filters.put("offset", page.getFirstResult());
		filters.put("pageSize", page.getPageSize());
		filters.put("lastRows", page.getFirstResult() + page.getPageSize());
		filters.put("sortColumns", pageRequest.getSortColumns());

		Map parameterObject = null;
		try {
			parameterObject = PropertyUtils.describe(pageRequest);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		filters.putAll((Map) parameterObject.get("filters"));

		RowBounds rowBounds = new RowBounds(page.getFirstResult(),
				page.getPageSize());
		List list = db().selectList(statementName, filters, rowBounds);
		page.setResult(list);
		return page;
	}

	public List<E> findByMap(final Map map) {
		return db().selectList(getPageSelect(), map);
	}

	public List<E> findBy(final Object... params) {
		return db().selectList(getPageSelect(), map(params));
	}

	public List<E> findRowBy(final int rowCount, final Object... params) {
		RowBounds rowBounds = new RowBounds(0, rowCount);
		return db().selectList(getPageSelect(), map(params), rowBounds);
	}
	
	public E findUniqueBy(final Object... params) {
		return (E) db().selectOne(getPageSelect(), map(params));
	}

	public boolean isPropertyUnique(final String property,
			final String orgValue, final String newValue) {
		return newValue.equals(orgValue)
				|| (findUniqueBy(property, newValue) == null);
	}

	public List findAll() {
		return db().selectList(getPageSelect(), map());
	}

	public Page findByPageRequest(final PageRequest pageRequest) {
		return pageQuery(getPageSelect(), pageRequest);
	}

	public boolean isUnique(final E entity, final String uniquePropertyNames) {
		throw new UnsupportedOperationException();
	}

	public void flush() {
		// DO NOTHING
	}

	/**
	 * 根据参数构造map，参数必须为偶数个，依次为key1，value1，key2，value2…….
	 * 
	 * @param datas
	 *            参数列表
	 * @return 构造出的map
	 */
	protected Map map(final Object... datas) {
		Assert.isTrue(datas.length % 2 == 0, "参数必须为偶数个");
		final Map map = new HashMap();
		for (int i = 0; i < datas.length; i += 2) {
			map.put(datas[i], datas[i + 1]);
		}
		return map;
	}
}
