/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.mytest.web.wap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mytest.utils.MyLogFactory;

@Controller
@RequestMapping(value = "/wap/error")
public class ErrorController {
	
	private static final Logger logger = MyLogFactory.getLog(ErrorController.class);
	
	@RequestMapping(value="/syserror", method={RequestMethod.GET})
	public Object error(String errormsg) {
		ModelAndView view = new ModelAndView();
		view.addObject("errormsg", errormsg);
		view.setViewName("error");
		return view;
	}
}
