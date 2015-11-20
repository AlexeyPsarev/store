package com.dataart.springtraining.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ControllerAdvice
public class ExceptionHandlerController
{
	private static final String ERR_MGS_KEY = "errMsg";
	
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception ex)
	{
		return new ModelAndView("errorPage", ERR_MGS_KEY, ex.getMessage());
	}
}
