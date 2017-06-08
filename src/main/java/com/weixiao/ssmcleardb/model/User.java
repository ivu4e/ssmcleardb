package com.weixiao.ssmcleardb.model;

/**
 * 表名：user
 * 备注：User
 */
@SuppressWarnings("serial")
public class User {

	//columns START
	//编号
	private Long id;
	//用户名
	private String name;
	//用户密码
	private String password;
	//手机号码
	private String mobile;
	//电子邮件
	private String email;
	//用户状态，默认为0，不可用，1为可用 0: 表示新建1：表示激活2：表示禁用3：表示删除
	private Byte status;
	//真实姓名
	private String trueName;
	//用户编码
	private String userCode;
	//头像相对路径
	private String photo;
	//创建时间
	private java.sql.Timestamp createTime;
	//最后修改时间
	private java.sql.Timestamp updateTime;
	//columns END
	
	//extend columns START
	
	//extend columns END

	public User(){
	}

	public User(Long id){
		this.id = id;
	}

	/**
	 * 编号
	 * @return
	 */
	public Long getId() {
		return this.id;
	}
	
	/**
	 * 编号
	 * @param value
	 */
	public void setId(Long value) {
		this.id = value;
	}
	/**
	 * 用户名
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 用户名
	 * @param value
	 */
	public void setName(String value) {
		this.name = value;
	}
	/**
	 * 用户密码
	 * @return
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * 用户密码
	 * @param value
	 */
	public void setPassword(String value) {
		this.password = value;
	}
	/**
	 * 手机号码
	 * @return
	 */
	public String getMobile() {
		return this.mobile;
	}
	
	/**
	 * 手机号码
	 * @param value
	 */
	public void setMobile(String value) {
		this.mobile = value;
	}
	/**
	 * 电子邮件
	 * @return
	 */
	public String getEmail() {
		return this.email;
	}
	
	/**
	 * 电子邮件
	 * @param value
	 */
	public void setEmail(String value) {
		this.email = value;
	}
	/**
	 * 用户状态，默认为0，不可用，1为可用 0: 表示新建1：表示激活2：表示禁用3：表示删除
	 * @return
	 */
	public Byte getStatus() {
		return this.status;
	}
	
	/**
	 * 用户状态，默认为0，不可用，1为可用 0: 表示新建1：表示激活2：表示禁用3：表示删除
	 * @param value
	 */
	public void setStatus(Byte value) {
		this.status = value;
	}
	/**
	 * 真实姓名
	 * @return
	 */
	public String getTrueName() {
		return this.trueName;
	}
	
	/**
	 * 真实姓名
	 * @param value
	 */
	public void setTrueName(String value) {
		this.trueName = value;
	}
	/**
	 * 用户编码
	 * @return
	 */
	public String getUserCode() {
		return this.userCode;
	}
	
	/**
	 * 用户编码
	 * @param value
	 */
	public void setUserCode(String value) {
		this.userCode = value;
	}
	/**
	 * 头像相对路径
	 * @return
	 */
	public String getPhoto() {
		return this.photo;
	}
	
	/**
	 * 头像相对路径
	 * @param value
	 */
	public void setPhoto(String value) {
		this.photo = value;
	}
	
	/**
	 * 创建时间
	 * @return
	 */
	public java.sql.Timestamp getCreateTime() {
		return this.createTime;
	}
	
	/**
	 * 创建时间
	 * @param value
	 */
	public void setCreateTime(java.sql.Timestamp value) {
		this.createTime = value;
	}
	
	/**
	 * 最后修改时间
	 * @return
	 */
	public java.sql.Timestamp getUpdateTime() {
		return this.updateTime;
	}
	
	/**
	 * 最后修改时间
	 * @param value
	 */
	public void setUpdateTime(java.sql.Timestamp value) {
		this.updateTime = value;
	}
}

