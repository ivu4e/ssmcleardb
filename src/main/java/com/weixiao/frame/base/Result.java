package com.weixiao.frame.base;

/**
 * 操作结果页，通过异常处理机制跳转，可以在所有需要的地方（DAO、Manager、Controller……）使用.
 *
 * @author xuqiao
 */
public final class Result extends RuntimeException {

	private static final long serialVersionUID = -372469097250506251L;
	
	public static final int TYPE_FAIL = 0;
	public static final int TYPE_SUCCESS = 1;
	public static final int TYPE_WARNING=2;
	
	
	
	private Result(final int type, final String message, final String backUrl, boolean needRefreshTree) {
		super(message);
		this.type = type;
		this.backUrl = backUrl; 
		this.needRefreshTree = needRefreshTree;
	}
	
	private Result(final int type, final String message, final String backUrl, boolean needRefreshTree, final Exception e) {
		super(message, e);
		this.type = type;
		this.backUrl = backUrl;
		this.needRefreshTree = needRefreshTree;
	}

	/**
	 * @param message
	 * @param e
	 */
	public static void fail(String message) {
		throw new Result(TYPE_FAIL, message, "", false);
	}
	 
	public static void fail(String message, Exception e) {
		throw new Result(TYPE_FAIL, message, "", false, e);
	}
	
	public static void fail(final String message, final String backUrl, final boolean needRefreshTree) {
		throw new Result(TYPE_FAIL, message, backUrl, needRefreshTree);
	}

	public static void fail(final String message, final String backUrl, final boolean needRefreshTree, final Exception e) {
		throw new Result(TYPE_FAIL, message, backUrl, needRefreshTree, e);
	}	
	
	
	
	/**
	 * 操作成功页.
	 */
	public static void success(final String message, final String backUrl, final boolean needRefreshTree) {
		throw new Result(TYPE_SUCCESS, message, backUrl, needRefreshTree);
	}
	
	/**
	 *操作部分成功部分失败了。 
	 */
	public static void warning(final String message, final String backUrl, final boolean needRefreshTree)
	{
		throw new Result(TYPE_WARNING, message, backUrl, needRefreshTree);
	}

	private String backUrl;

	private boolean needRefreshTree = true;



	private int type;

	public String getBackUrl() {
		return backUrl;
	}

	public int getType() {
		return type;
	}

	public boolean isNeedRefreshTree() {
		return needRefreshTree;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public void setNeedRefreshTree(boolean needRefreshTree) {
		this.needRefreshTree = needRefreshTree;
	}

	public void setType(final int type) {
		this.type = type;
	}
}
