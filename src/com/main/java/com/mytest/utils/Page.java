package com.mytest.utils;

import java.util.List;

/**
 * 一页数据
 * 
 * @author eric
 * @param <T>
 * @date 2012-6-18
 */
public class Page<T> {
	/**
	 * 数据总数
	 */
	private int total;
	
	/**
	 * 要显示的数据
	 */
	private List<T> datas;

	public Page() {
		
	}
	
	public Page(List<T> datas, int total) {
		this.datas = datas;
		this.total = total;
	}
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	@Override
	public String toString() {
		return "Page [total=" + total + ", datas=" + datas + "]";
	}	
	
}
