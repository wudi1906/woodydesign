package com.mytest.utils.sms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.mytest.utils.DataUtils;
import com.mytest.utils.StringUtils;
import com.mytest.utils.http.HttpClient4Util;
import com.mytest.utils.http.HttpParameter;
import com.mytest.utils.http.HttpResp;
import com.mytest.utils.json.JsonUtils;
import com.mytest.utils.security.Digest;

/**
 * 调用3代通知系统抽象类
 * 
 * @author eric
 * @date 2013-12-13
 */
public class NotifyClient3GUtils {
	
	private static final Log logger = LogFactory.getLog(NotifyClient3GUtils.class);
	
	/**
	 * 三代短信通知配置信息
	 */
	private Map<String,NC3GConfig> NC3GConfigMap;

	/**
	 * 发送短信方法
	 * @param recipients 接收人列表,多个的话用逗号分隔
	 * @param content 通知参数，json格式，用于生成具体通知内容
	 * @param extNo 主号码+内部扩展码=总扩展码
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public NotifyResult sendSms(String recipients, String content, String extNo) throws ClientProtocolException, IOException, DocumentException {
		// 默认取第一条通知规则
		NC3GConfig config = null;
		for(Map.Entry<String,NC3GConfig> configEntry : NC3GConfigMap.entrySet()){
			if(!DataUtils.isEmpty(configEntry)){
				config = configEntry.getValue();
			}
		}
		return sendSms(config, recipients, content, extNo);
	}
	
	/**
	 * 发送短信方法
	 * @param rule 通知规则
	 * @param recipients 接收人列表,多个的话用逗号分隔
	 * @param content 通知参数，json格式，用于生成具体通知内容
	 * @param extNo 主号码+内部扩展码=总扩展码
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public NotifyResult sendSms(String rule, String recipients, String content, String extNo) throws ClientProtocolException, IOException, DocumentException {
		// 默认取第一条通知规则
		NC3GConfig config = NC3GConfigMap.get(rule);
		return sendSms(config, recipients, content, extNo);
	}

		
	private NotifyResult sendSms(NC3GConfig config, String recipients, String content, String extNo) throws ClientProtocolException, IOException, DocumentException {
		HttpClient4Util util = new HttpClient4Util();
		HttpParameter httpParameter = new HttpParameter();
		httpParameter.add("username", config.getUserName());
		httpParameter.add("notifyRuleName", config.getRuleName());
		httpParameter.add("recipients", recipients);
		if(!DataUtils.isEmpty(extNo)){
			httpParameter.add("extNo", (config.getMainSectionNo() + extNo));
		}
		Map<String, Object> messages = new HashMap<String, Object>();
		if (!DataUtils.isEmpty(content)) {
			messages.put("message", content);
			httpParameter.add("content", JsonUtils.toJson(messages));
		}
		// 参数做MD5 Hmac签名
		String sign = "";
		if (!DataUtils.isEmpty(extNo)) {
			sign = Digest.signMD5((config.getUserName() + config.getRuleName() + recipients + config.getAppSecret() + (config.getMainSectionNo() + extNo)), "UTF-8");
		} else {
			sign = Digest.signMD5((config.getUserName() + config.getRuleName() + recipients + config.getAppSecret()), "UTF-8");
		}
		
		httpParameter.add("sign", sign);
		HttpResp httpResp = util.doPost(config.getUrl(), httpParameter, "utf-8");
		NotifyResult result = new NotifyResult();
		if (!DataUtils.isEmpty(httpResp)) {
			String resXml = httpResp.getText("utf-8");
			// 返回数据
			logger.info("调用3代通知系统返回XML。resXml为" + resXml);
			if (!StringUtils.isEmpty(resXml)) {
				Document doc = null;
				doc = DocumentHelper.parseText(resXml);
				Element rootElt = doc.getRootElement();
				String errorCodeStr = rootElt.attributeValue("errorCode");
				String msgIdStr = rootElt.attributeValue("msgId");
				String extNoStr = rootElt.attributeValue("extNo");
				logger.info("调用3代通知系统返回errorCode为:" + errorCodeStr + ";msgId为:"
						+ msgIdStr + ";extNo为:" + extNoStr);
				result.setErrorCode(Integer.parseInt(errorCodeStr.trim()));
				result.setMsgId(msgIdStr);
				result.setExtNo(extNoStr);
			} else {
				logger.error("调用3代通知系统返回XML为空。resXml为" + resXml);
			}
		} else {
			logger.error("调用3代通知系统返回HttpResp为空。httpResp为" + httpResp);
		}
		return result;
	}

	public void setNC3GConfigMap(Map<String, NC3GConfig> nC3GConfigMap) {
		NC3GConfigMap = nC3GConfigMap;
	}
	
	
}
