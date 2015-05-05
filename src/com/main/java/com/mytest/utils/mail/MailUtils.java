package com.mytest.utils.mail;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailUtils {

	private JavaMailSenderImpl mailSender;

	public void sendSimpleMail(String to, String title, String content) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject(title);
		mailMessage.setText(content);
		mailMessage.setFrom(this.mailSender.getUsername());
		mailSender.send(mailMessage);
	}

	public void sendHtmlMail(String to, String title, String content)
			throws MessagingException {
		MimeMessage mailMessage = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "UTF-8");
		// messageHelper.setTo("m47121@163.com");// 测试使用
		messageHelper.setTo(to);
		messageHelper.setSubject(title);
		messageHelper.setText(content, true);
		mailMessage.setFrom(new InternetAddress(this.mailSender.getUsername()));
		mailSender.send(mailMessage);
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}
}