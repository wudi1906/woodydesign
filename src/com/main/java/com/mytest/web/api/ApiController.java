/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.mytest.web.api;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mytest.entity.TransactionInfo;
import com.mytest.utils.MyLogFactory;

@Controller
@RequestMapping(value = "/api")
public class ApiController {
	
	private static final Logger logger = MyLogFactory.getLog(ApiController.class);
	
	@RequestMapping(value="/first", method={RequestMethod.GET})
	public Object list() {
		ModelAndView view = new ModelAndView();
		
		logger.info(" == hahatest == ");
		TransactionInfo transactionInfo = TransactionInfo.objById(3);
		view.addObject("name", transactionInfo.getName());
		view.setViewName("ok");
		logger.info(" == TransactionInfo == " + transactionInfo.getName());
		return view;
	}
}
