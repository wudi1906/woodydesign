package com.mytest.utils.dborder;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库排序
 * 
 * @author eric
 * @date 2012-11-7
 */
public class Order {
	/**
	 * 需要参加排序的字段
	 */
	private List<OrderField> fields;

	/**
	 * 构造器
	 */
	public Order() {
		fields = new ArrayList<OrderField>();
	}
	
	/**
	 * 获取要排序的字段
	 * 
	 * @return 要排序的字段
	 */
	public List<OrderField> getFields() {
		return fields;
	}

	/**
	 * 设置要排序的字段
	 * 
	 * @param fields 
	 *           要排序的字段
	 */
	public void setFields(List<OrderField> fields) {
		this.fields = fields;
	}
	
	public void add(OrderField field) {
		if (field != null) {
			fields.add(field);
		}
	}
}
