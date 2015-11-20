package com.dataart.springtraining.controllers;

import com.dataart.springtraining.auxiliary.HeaderSetter;
import com.dataart.springtraining.domain.ApplicationPkg;
import com.dataart.springtraining.service.ApplicationPkgService;
import com.dataart.springtraining.service.UserService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

@org.springframework.stereotype.Controller
public class DetailsController implements Controller
{
	@Resource
	private ApplicationPkgService appService;
	
	@Resource
	private UserService userService;

	private static final String POPULAR_KEY = "popularApps";
	private static final String AUTHOR_KEY = "author";
	private static final String APPLICATION_ID_KEY = "app";
	private static final String APPLICATION_KEY = "app";
		
	@Override
	@RequestMapping(value = "/details.htm", method = RequestMethod.GET)
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
	{
		if (request.getSession(false).getAttribute("userId") == null)
			return new ModelAndView("redirect:/");
		HeaderSetter.setHeaders(response);
		
		Integer id = Integer.parseInt(request.getParameter(APPLICATION_ID_KEY));
		Map<String, Object> model = new HashMap<>();
		model.put(POPULAR_KEY, appService.findMostPopular().iterator());
		ApplicationPkg pkg = appService.findById(id);
		model.put(APPLICATION_KEY, pkg);
		model.put(AUTHOR_KEY, userService.find(pkg.getAuthor()));
		return new ModelAndView("details", model);
	}
}
