package com.mytest.utils.sms;

/**
 * 
 * 短信中心应答实体
 * 
 * @author tianshi.li
 * 
 */
public class NotifyResult implements java.io.Serializable {

	private static final long serialVersionUID = -7266472330300293654L;
	/**
	 * errorCode说明： 
	 * 0 发送成功 
	 * 1 发送不成功（新建待发、正在处理、重试、失败） 
	 * 200 验证不通过，请检查用户名、签名等 
	 * 100 接收人不合法，请检查接收人地址（邮箱、手机号） 
	 * 101 通知规则不合法，请检查通知规则是否存在、是否启用 
	 * 102 通知内容生成错误，请检查通知模版与消息参数是否匹配 
	 * 900 其他错误
	 */
	private Integer errorCode;
	
	private String msgId;
	/**
	 * 上行所用扩展号码
	 */
	private String extNo;

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getExtNo() {
		return extNo;
	}

	public void setExtNo(String extNo) {
		this.extNo = extNo;
	}

}
