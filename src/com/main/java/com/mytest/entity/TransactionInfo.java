package com.mytest.entity;

import halo.query.annotation.Column;
import halo.query.annotation.Id;
import halo.query.annotation.Table;
import halo.query.model.BaseModel;

import org.apache.log4j.Logger;

import com.mytest.utils.MyLogFactory;

/**
 * 交易订单(收银台使用，主要是起辅助作用)
 * 
 * @author eric
 * @date 2014-3-3
 */
@Table(name = "app_woodydesign.classes")
public class TransactionInfo extends BaseModel {
	private static final Logger logger = MyLogFactory.getLog(TransactionInfo.class);
	 
	/**
	 * 主键
	 */
	@Id
	@Column("ID")
	private int id;
	
	/**
	 * 商户账户编号
	 */
	@Column("NAME")
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static TransactionInfo objById(long id) {
		TransactionInfo info = getQuery().objById(TransactionInfo.class, id);
		return info;
	}
	
	
}
