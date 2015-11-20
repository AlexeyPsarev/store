package com.dataart.springtraining.controllers;

import com.dataart.springtraining.domain.User;
import com.dataart.springtraining.service.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.stereotype.Controller
public class RegistrationController
{
	@Resource
	private UserService service;
	
	@Autowired
	private PasswordEncoder encoder;
	
	private static final Pattern USERNAME_PATTERN = Pattern.compile(
		"^(?=.{4,20}$)(?![_.0-9])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
	private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.{8,}$)([\\S]*)$");
	private static final Pattern FULLNAME_PATTERN = Pattern.compile("^[a-zA-Z ]{1,}$");
		
	private static final String USERNAME_KEY = "username";
	private static final String PASSWORD_KEY = "password";
	private static final String FULLNAME_KEY = "fullname";
	private static final String CONFIRMATION_KEY = "confirmation";
	private static final String BAD_USERNAME_KEY = "badUsername";
	private static final String BAD_FULLNAME_KEY = "badFullname";
	private static final String BAD_PASSWORD_KEY = "badPassword";
	private static final String CONFIRMATION_ERROR_KEY = "confirmErr";
	private static final String CANNOT_CREATE_KEY = "cannotCreate";
			
	private static final String BAD_USERNAME = "Forbidden username. Please, try again";
	private static final String BAD_FULLNAME = "Forbidden full name. Please, try again";
	private static final String BAD_PASSWORD = "Forbidden password. Please, try again";
	private static final String CONFIRMATION_ERROR = "Password doesn't match the confirmation. Please, try again";
	private static final String CANNOT_CREATE = "User with such name already exists";
	
	@RequestMapping(value = "/registrationForm.htm", method = RequestMethod.GET)
	public ModelAndView showRegistrationForm(HttpServletRequest request, HttpServletResponse response)
	{
		return new ModelAndView("registration");
	}
	
	@RequestMapping(value = "/register.htm", method = RequestMethod.POST)
	public ModelAndView performRegistration(HttpServletRequest request, HttpServletResponse response)
	{
		String username = request.getParameter(USERNAME_KEY);
		String fullname = request.getParameter(FULLNAME_KEY);
		String password = request.getParameter(PASSWORD_KEY);
		String confirmation = request.getParameter(CONFIRMATION_KEY);
		Map<String, Object> model = new HashMap<>();
		Matcher m;
		m = USERNAME_PATTERN.matcher(username);
		if (!m.matches())
			model.put(BAD_USERNAME_KEY, BAD_USERNAME);
		m = FULLNAME_PATTERN.matcher(fullname);
		if (!m.matches())
			model.put(BAD_FULLNAME_KEY, BAD_FULLNAME);
		m = PASSWORD_PATTERN.matcher(password);
		if (!m.matches())
			model.put(BAD_PASSWORD_KEY, BAD_PASSWORD);
		if (!password.equals(confirmation))
			model.put(CONFIRMATION_ERROR_KEY, CONFIRMATION_ERROR);
		if (!model.isEmpty())
		{
			model.put(USERNAME_KEY, username);
			model.put(FULLNAME_KEY, fullname);
			return new ModelAndView("registration", model);
		}

		User user = new User(username, encoder.encode(password), fullname);
		if (service.canCreate(user))
		{
			service.save(user);
			request.getSession(false).invalidate();
			request.getSession(true).setAttribute("userId", user.getId());
			return new ModelAndView("redirect:/home.htm");
		}
		else
		{
			model.put(CANNOT_CREATE_KEY, CANNOT_CREATE);
			return new ModelAndView("registration", model);
		}
	}
}
