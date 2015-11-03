package com.dataart.springtraining.controllers;

import com.dataart.springtraining.dao.UserRepository;
import com.dataart.springtraining.domain.User;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

@Service
public class RegistrationController implements Controller
{
	@Resource
	private UserRepository dao;
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) 
	{
		User user = new User(
			request.getParameter("username"),
			request.getParameter("password"),
			"salt",
			request.getParameter("fullname"));
		dao.saveUser(user);
		return null;
	}
}
