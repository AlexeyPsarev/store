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
public class LoginController implements Controller
{
	@Resource
	private UserService service;
	
	private static final String AUTH_FAILED = "Incorrect username or password. Please, try again";
	
	@Override
	@RequestMapping(value = "login.htm", method = RequestMethod.POST)
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String username = request.getParameter("username");
		if (service.authenticate(username, request.getParameter("password")))
		{
			User user = service.find(username);
			Map<String, Object> model = new HashMap<>();
			model.put("userId", user.getId());
			return new ModelAndView("home", model);
		}
		else
		{
			request.setAttribute("errMsg", AUTH_FAILED);
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return null;
		}
    }
}
