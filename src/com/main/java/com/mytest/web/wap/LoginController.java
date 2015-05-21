/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.mytest.web.wap;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mytest.entity.TransactionInfo;
import com.mytest.utils.MyLogFactory;
import com.mytest.utils.StringUtils;

@Controller
@RequestMapping(value = "/wap/login")
public class LoginController {
	
	private static final Logger logger = MyLogFactory.getLog(LoginController.class);
	
	@RequestMapping(value="/login_index", method={RequestMethod.GET})
	public Object loginIndex(HttpSession session) {
		ModelAndView view = new ModelAndView();
		view.setViewName("login_index");
		logger.info(" == loginindex == ");
		return view;
	}
	
	@RequestMapping(value="/do_login", method={RequestMethod.POST})
	public Object doLogin(@RequestParam(value = "username", required = true) String username, 
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "token", required = true) String token, 
			HttpSession session) {
		ModelAndView view = new ModelAndView();
		
		
		if(!StringUtils.equals("aa", password)){
			logger.error(" == LoginController doLogin password error 密码错误 == ");
			view.setViewName("error");
			view.addObject("errormsg", "密码输入错误");
			return view;
		}
		
		logger.info(" == hahatest == ");
		TransactionInfo transactionInfo = TransactionInfo.objById(2);
		view.addObject("name", transactionInfo.getName());
		view.setViewName("ok");
		logger.info(" == TransactionInfo == " + transactionInfo.getName());
		return view;
	}
}
