package com.weixiao.frame.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import com.weixiao.frame.page.Page;
import com.weixiao.frame.page.PageRequest;

/**
 * Manager基类.
 * 
 * @author lipw
 *
 * @param <E> Entity的Class
 * @param <PK> 主键的Class
 */
public abstract class BaseManager <E, PK extends Serializable> {

	protected Log log = LogFactory.getLog(getClass());

	protected abstract EntityDao getEntityDao();

	/**
	 * 
	 * @param ids ,逗号分隔的id集合
	 * @return
	 */
	public List<E> getByIds(String ids) {
		return getEntityDao().getByIds(ids);
	}
	
	/**
	 * id集合
	 * @param list
	 * @return
	 */
	public List<E> getByIds(List<Long> list) {
		return getEntityDao().getByIds(list);
	}
	
	public Page findByPageRequest(final PageRequest pr) {
		return getEntityDao().findByPageRequest(pr);
	}
	
	public E get(final Long id) {
		return (E) getEntityDao().getById(id);
	}

	public List<E> findAll() {
		return getEntityDao().findAll();
	}

	public int save(final E entity) {
		return getEntityDao().save(entity);
	}

	public int update(final E entity) {
		return getEntityDao().update(entity);
	}

	public int saveOrUpdate(final E entity) {
		return getEntityDao().saveOrUpdate(entity);
	}

	public void removeById(final PK id) {
		getEntityDao().deleteById(id);
	}

	public void removeByIds(final String ids) {
		getEntityDao().deleteByIds(ids);
	}
	
	/**
	 * 删除对象
	 * @param entity
	 */
	public void remove(BaseEntity entity){
		getEntityDao().remove(entity);
	}
	
	/**
	 * 根据id集合 批量删除
	 * 
	 * @param list
	 */
	public void deleteByIds(List<Long> list) {
		getEntityDao().deleteByIds(list);
	}
	
	public void deleteByLocalIds(String localIds) {
		getEntityDao().deleteByLocalIds(localIds);
	}
	
	public void deleteByLocalIds(List<String> list) {
		getEntityDao().deleteByLocalIds(list);
	}
		
	public void deleteBy(final Object... params){
		this.getEntityDao().deleteBy(params);
	}
	
	/**
	 * 根据id集合 批量修改
	 * @param list
	 */
	public void updateByObjects(String id){
		this.getEntityDao().updateByObjects(id);
	}
	
	public void updateByIds(List<Long> list){
		this.getEntityDao().updateByIds(list);
	}
	

	/**
	 * 判断指定的字段是否唯一.
	 *
	 * @param property 字段名称
	 * @param orgValue 字段原有值
	 * @param newValue 字段更新值
	 * @return 唯一返回true，否则返回false
	 * isPropertyUnique("username", "", "minwh")
	 */
	public boolean isPropertyUnique(final String property, final String orgValue, final String newValue) {
		return getEntityDao().isPropertyUnique(property, orgValue, newValue);
	}
	
	public boolean isUnique(final E entity, final String uniquePropertyNames) {
		return getEntityDao().isUnique(entity, uniquePropertyNames);
	}

	protected void removeBy(final Object... params) {
		getEntityDao().deleteBy(params);
	}

	public List<E> findBy(final Object... params) {
		return find(map(params));
	}

	protected List<E> find(final Map map) {
		return getEntityDao().findByMap(map);
	}
	
	public List<E> findRowBy(final int rowCount, final Object... params) {
		return getEntityDao().findRowBy(rowCount, params);
	}

	public E findUniqueBy(final Object... params) {
		return (E) getEntityDao().findUniqueBy(params);
	}
	
	/**
	 * 根据参数构造map，参数必须为偶数个，依次为key1，value1，key2，value2…….
	 * @param datas 参数列表
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
