package com.dataart.springtraining.controllers;

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

	@Override
	@RequestMapping(value = "details.htm", method = RequestMethod.GET)
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
	{
		Integer id = Integer.parseInt(request.getParameter("app"));
		Map<String, Object> model = new HashMap<>();
		model.put("popularApps", appService.findMostPopular().iterator());
		ApplicationPkg pkg = appService.findById(id);
		model.put("app", pkg);
		model.put("author", userService.find(pkg.getAuthor()));
		return new ModelAndView("details", model);
	}
	
}
