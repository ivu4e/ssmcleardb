package com.weixiao.frame.base;

import java.sql.Timestamp;

import com.weixiao.frame.util.DateTimeUtils;

//静态导入日期转换方法

/**
 * Entity基类，提供一些必要的工具方法和常量.
 * 
 * @author lipw
 * 
 */
@SuppressWarnings("serial")
public abstract class BaseEntity implements java.io.Serializable {
	
	@JsonExclude(ignore=true)
	protected Integer status = 1;
	@JsonExclude(ignore=true)
	protected Timestamp createTime;
	@JsonExclude(ignore=true)
	protected Timestamp updateTime;
	@JsonExclude(ignore=true)
	protected String localId;//本地数据库id

	protected static final String DATE_FORMAT = "yyyy-MM-dd";

	protected static final String TIME_FORMAT = "HH:mm:ss";
	
	protected static final String DATE_FORMAT_SORT = "MM月dd号";

	protected static final String TIME_FORMAT_SORT = "HH:mm";

	protected static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	protected static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

	// 扩展字段
	@JsonExclude(ignore=true)
	protected String info;

	public String date2String(final java.util.Date date, final String dateFormat) {
		return DateTimeUtils.format(date, dateFormat);
	}

	public <T extends java.util.Date> T string2Date(final String dateString,
			final String dateFormat, final Class<T> targetResultType) {
		return DateTimeUtils.parse(dateString, dateFormat, targetResultType);
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Long getId(){
		return 0L;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

    public String getCreateTimeString() {
        return date2String(getCreateTime(), DATE_TIME_FORMAT);
    }
    public void setCreateTimeString(String value) {
        setCreateTime(string2Date(value, DATE_TIME_FORMAT,java.sql.Timestamp.class));
    }

    public String getUpdateTimeString() {
        return date2String(getUpdateTime(), DATE_TIME_FORMAT);
    }
    public void setUpdateTimeString(String value) {
        setUpdateTime(string2Date(value, DATE_TIME_FORMAT,java.sql.Timestamp.class));
    }

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}
	
}
