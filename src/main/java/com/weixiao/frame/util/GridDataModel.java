package com.weixiao.frame.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询数据封装对象
 * @author lipw
 * @date   2017年3月29日上午10:03:38
 */
public class GridDataModel {
	//总的记录数
	private int total = 0 ;
	//当页记录数据
	private List rows;
	//表字段信息
	private List colmodel;

	public GridDataModel()
	{
		rows = new ArrayList();
		colmodel = new ArrayList();
	}
	//表字段信息
	public List getColmodel()
	{
		return colmodel;
	}

	public void setColmodel(List list)
	{
		colmodel = list;
	}
	//当页记录数据
	public List getRows()
	{
		return rows;
	}

	public void setRows(List list)
	{
		rows = list;
	}
	//总的记录数
	public int getTotal()
	{
		return total;
	}

	public void setTotal(int i)
	{
		total = i;
	}
}
