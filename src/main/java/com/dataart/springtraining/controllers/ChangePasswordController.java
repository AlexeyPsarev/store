package com.dataart.springtraining.controllers;

import com.dataart.springtraining.domain.User;
import com.dataart.springtraining.service.UserService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChangePasswordController
{
	@Autowired
	private PasswordEncoder encoder;
	
	@Resource
	private UserService service;
	
	private static final String AUTH_FAILED = "Incorrect password. Please, try again";
	private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.{8,}$)([\\S]*)$");
	private static final String BAD_PASSWORD = "Forbidden password. Please, try again";
	private static final String CONFIRMATION_ERROR = "Password doesn't match the confirmation. Please, try again";
	
	@RequestMapping(value = "changePassword.htm", method = RequestMethod.GET)
	public ModelAndView showPage(HttpServletRequest request, HttpServletResponse response)
	{
		if (request.getSession(false).getAttribute("userId") == null)
			return new ModelAndView("redirect:/");
		response.setHeader("pragma", "no-cache");
		response.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
		response.setHeader("Expires", "0"); 
		return new ModelAndView("password");
	}
	
	@RequestMapping(value = "newPassword.htm", method = RequestMethod.POST)
	public ModelAndView setNewPassword(HttpServletRequest request, HttpServletResponse response)
	{
		Integer id = (Integer)request.getSession(false).getAttribute("userId");
		if (id == null)
			return new ModelAndView("redirect:/");
		response.setHeader("pragma", "no-cache");
		response.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
		response.setHeader("Expires", "0");
		
		User user = service.find(id);
		if (service.authenticate(user.getUsername(), request.getParameter("oldPassword")))
		{
			String password = request.getParameter("password");
			String confirmation = request.getParameter("confirmation");
			Matcher m = PASSWORD_PATTERN.matcher(password);
			if (!m.matches())
				return new ModelAndView("password", "newPassErr", BAD_PASSWORD);
			if (!password.equals(confirmation))
				return new ModelAndView("password", "confirmErr", CONFIRMATION_ERROR);
			user.setPassword(encoder.encode(password));
			service.save(user);
			return new ModelAndView("passwordChanged");
		}
		else
			return new ModelAndView("password", "oldPassErr", AUTH_FAILED);
	}
}
