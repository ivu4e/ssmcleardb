package com.weixiao.frame.base;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据缓存
 * @author lipw
 *
 */
public class ConstantDict {
	//默认用户头像
	public final static String AvatarPath = "/static/image/avatar.jpeg";
	

	//md5 盐值
	public final static String MD5_SALT = "qiwue283";
	
	public final static String SESSION_USER = "SESSION_USER";
	
	//分类JSON数据缓存
	private static Map<String, String> _categoryCache = new HashMap<String, String>();
	
	//单例
	private final static ConstantDict _instance = new ConstantDict();

	//错误码及对应的消息缓存
	private static Map<Long, String> _errorMap;
	
	/// <summary>
	/// Prevents a default instance of the
	/// <see cref="Singleton"/> class from being created.
	/// </summary>
	private ConstantDict() {
		_errorMap = new HashMap<Long, String>();
		Field[] fields = BaseCodeMessage.class.getFields();
		for (Field fld : fields){
			CodeMessage cm = (CodeMessage)fld.getAnnotation(CodeMessage.class);
			try {
				if (cm != null){
					Object obj = fld.get(null);
					Long key =  (Long)obj;
					_errorMap.put(key, cm.msg());
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/// <summary>
	/// Gets the instance.
	/// </summary>
	public static ConstantDict getInstance() {
		return _instance;
	}
	
	/**
	 * 获取分类JSON数据缓存
	 * @return
	 */
	public Map<String, String> GetCategoryCache(){
		return _categoryCache;
	}
	
	/**
	 * 根据错误码获取对应的消息内容
	 * @param errCode
	 * @return
	 */
	public String getErrorMessage(Long errCode){
		return _errorMap.get(errCode);
	}
	
	/**
	 * 获取错误码及对应的消息缓存Map
	 * @return
	 */
	public Map<Long, String> getErrorMap(){
		return _errorMap;
	}

}
