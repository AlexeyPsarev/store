package com.dataart.springtraining.controllers;

import com.dataart.springtraining.domain.User;
import com.dataart.springtraining.service.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

@org.springframework.stereotype.Controller
public class RegistrationController
{
	@Resource
	private UserService service;
	
	private static final PasswordEncoder ENCODER = new StandardPasswordEncoder();
	private static final Pattern USERNAME_PATTERN = Pattern.compile(
		"^(?=.{4,20}$)(?![_.0-9])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
	private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.{8,}$)([\\S]*)$");
	private static final Pattern FULLNAME_PATTERN = Pattern.compile("^[a-zA-Z ]{1,}$");
	private static final String BAD_USERNAME = "Forbidden username. Please, try again";
	private static final String BAD_FULLNAME = "Forbidden full name. Please, try again";
	private static final String BAD_PASSWORD = "Forbidden password. Please, try again";
	private static final String CONFIRMATION_ERROR = "Password doesn't match the confirmation. Please, try again";
	private static final String CANNOT_CREATE = "User with such name already exists";
	
	@RequestMapping(value = "registrationForm.htm", method = RequestMethod.GET)
	public ModelAndView showRegistrationForm(HttpServletRequest request, HttpServletResponse response)
	{
		return new ModelAndView("registration");
	}
	
	@RequestMapping(value = "register.htm", method = RequestMethod.POST)
	public ModelAndView performRegistration(HttpServletRequest request, HttpServletResponse response)
	{
		String username = request.getParameter("username");
		String fullname = request.getParameter("fullname");
		String password = request.getParameter("password");
		String confirmation = request.getParameter("confirmation");
		Map<String, Object> model = new HashMap<>();
		Matcher m;
		m = USERNAME_PATTERN.matcher(username);
		if (!m.matches())
			model.put("badUsername", BAD_USERNAME);
		m = FULLNAME_PATTERN.matcher(fullname);
		if (!m.matches())
			model.put("badFullname", BAD_FULLNAME);
		m = PASSWORD_PATTERN.matcher(request.getParameter("password"));
		if (!m.matches())
			model.put("badPassword", BAD_PASSWORD);
		if (!password.equals(confirmation))
			model.put("confirmErr", CONFIRMATION_ERROR);
		if (!model.isEmpty())
		{
			model.put("username", username);
			model.put("fullname", fullname);
			return new ModelAndView("registration", model);
		}

		User user = new User(request.getParameter("username"), ENCODER.encode(password), request.getParameter("fullname"));
		if (service.canCreate(user))
		{
			service.create(user);
			request.getSession(true).setAttribute("userId", user.getId());
			return new ModelAndView("redirect:/home.htm");
		}
		else
		{
			model.put("cannotCreate", CANNOT_CREATE);
			return new ModelAndView("registration", model);
		}
	}
}
