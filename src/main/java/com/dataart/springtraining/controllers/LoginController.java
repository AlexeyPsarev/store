package com.dataart.springtraining.controllers;

import com.dataart.springtraining.domain.User;
import com.dataart.springtraining.service.UserService;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@org.springframework.stereotype.Controller
public class LoginController
{
	@Resource
	private UserService service;
	
	private static final String AUTH_FAILED = "Incorrect username or password. Please, try again";
	
	@RequestMapping(value = "loginForm.htm", method = RequestMethod.GET)
	public ModelAndView showLoginForm(HttpServletRequest request, HttpServletResponse response)
	{
		return new ModelAndView("login");
	}
	
	@RequestMapping(value = "login.htm", method = RequestMethod.POST)
    public ModelAndView performLogin(HttpServletRequest request, HttpServletResponse response)
	{
		String username = request.getParameter("username");
		if (service.authenticate(username, request.getParameter("password")))
		{
			User user = service.find(username);
			request.getSession(true).setAttribute("userId", user.getId());
			return new ModelAndView("redirect:/home.htm");
		}
		else
		{
			return new ModelAndView("login", "errMsg", AUTH_FAILED);
		}
    }
}
