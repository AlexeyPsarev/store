package com.dataart.springtraining.controllers;

import com.dataart.springtraining.domain.User;
import com.dataart.springtraining.service.UserService;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@org.springframework.stereotype.Controller
public class LoginController
{
	@Resource
	private UserService service;
	
	private static final String USERNAME_KEY = "username";
	private static final String PASSWORD_KEY = "password";
	
	private static final String ERR_MSG_KEY = "errMsg";
	private static final String AUTH_FAILED = "Incorrect username or password. Please, try again";
	
	@RequestMapping(value = "/loginForm.htm", method = RequestMethod.GET)
	public ModelAndView showLoginForm(HttpServletRequest request, HttpServletResponse response)
	{
		return new ModelAndView("login");
	}
	
	@RequestMapping(value = "/login.htm", method = RequestMethod.POST)
    public ModelAndView performLogin(HttpServletRequest request, HttpServletResponse response)
	{
		String username = request.getParameter(USERNAME_KEY);
		if (service.authenticate(username, request.getParameter(PASSWORD_KEY)))
		{
			User user = service.find(username);
			request.getSession(false).invalidate();
			request.getSession(true).setAttribute("userId", user.getId());
			return new ModelAndView("redirect:/home.htm");
		}
		else
		{
			return new ModelAndView("login", ERR_MSG_KEY, AUTH_FAILED);
		}
    }
}
