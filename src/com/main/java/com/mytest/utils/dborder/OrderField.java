package com.mytest.utils.dborder;

/**
 * 数据库排序字段
 * 
 * @author eric
 * @date 2012-11-7
 */
public class OrderField {
	/**
	 * 排序字段
	 */
	private String field;
	
	/**
	 * true：倒序；false:升序
	 */
	private boolean reverse;

	/**
	 * 获取排序字段
	 * 
	 * @return 排序字段
	 */
	public String getField() {
		return field;
	}

	/**
	 * 设置排序字段
	 * 
	 * @param field
	 *          排序字段
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * 获取倒序还是升序
	 * 
	 * @return true：倒序；false:升序
	 */
	public boolean isReverse() {
		return reverse;
	}

	/**
	 * 设置倒序还是升序
	 * 
	 * @param reverse
	 *            true：倒序；false:升序
	 */
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
}
