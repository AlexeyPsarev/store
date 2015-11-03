package com.dataart.springtraining.controllers;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginController implements Controller
{
	@Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
	{
		Map<String, Object> model = new HashMap<>();
		//model.put("key", value);
		return new ModelAndView(/*"view", model*/);
    }
}
