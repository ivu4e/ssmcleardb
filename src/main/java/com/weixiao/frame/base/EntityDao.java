package com.weixiao.frame.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.weixiao.frame.page.Page;
import com.weixiao.frame.page.PageRequest;

/**
 * DAO接口.
 * 
 * @author xuqiao
 *
 * @param <E> 对应的Entity的Class
 * @param <PK> 主键Class
 */
public interface EntityDao<E, PK extends Serializable> {

	Page findByPageRequest(final PageRequest pr);
	
	Object getById(PK id);
	
	public List<E> getByIds(String ids);
	
	public List<E> getByIds(List<Long> list);

	void deleteById(PK id);

	void deleteBy(Object... params);
	
	void deleteByIds(String ids);
	
	void deleteByLocalIds(String localIds);
	
	/**
	 * 根据id集合 批量删除
	 * 
	 * @param list
	 */
	void deleteByIds(List<Long> list);
	
	void deleteByLocalIds(List<String> list);
	
	/**
	 * 删除对象
	 * @param entity
	 */
	void remove(BaseEntity entity);
	
	int save(E entity);

	int update(E entity);
	
	int updateByLocalId(E entity);

	int saveOrUpdate(E entity);

	boolean isUnique(E entity, String uniquePropertyNames);

	boolean isPropertyUnique(String property, String orgValue, String newValue);
	
	List<E> findAll();

	List<E> findBy(final Object... params);

	List<E> findRowBy(final int rowCount, final Object... params);
	
	E findUniqueBy(final Object... params);

	List<E> findByMap(Map map);
	
	void flush();
	
    /**
     * 根据id集合 批量修改
     * @param id
     */
	void updateByObjects(String id);
	
	void updateByIds(List<Long> list);

}
