package com.mytest.utils.sms;

/**
 * 调用3代通知系统配置
 * 
 * @author tianshi.li
 * @date 2014-08-10
 */
public class NC3GConfig {

	/**
	 * 3代通知系统服务之间通信的工作密钥
	 */
	private String appSecret;
	/**
	 * 3代通知系统主号码
	 */
	private String mainSectionNo;
	/**
	 * 3代通知系统服务之间通信的通知规则名词
	 */
	private String ruleName;
	/**
	 * 3代通知系统URL
	 */
	private String url;
	/**
	 * 3代通知系统服务之间通信的登录名（用户名）
	 */
	private String userName;

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getMainSectionNo() {
		return mainSectionNo;
	}

	public void setMainSectionNo(String mainSectionNo) {
		this.mainSectionNo = mainSectionNo;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
