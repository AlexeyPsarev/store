package com.dataart.springtraining.controllers;

import com.dataart.springtraining.domain.ApplicationPkg;
import com.dataart.springtraining.service.ApplicationPkgService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

@org.springframework.stereotype.Controller
public class HomeController implements Controller
{
	@Resource
	private ApplicationPkgService service;
	
	private static final String DEFAULT_CATEGORY = "Games";
	
	@Override
	@RequestMapping(value = "home.htm", method = RequestMethod.GET)
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
	{
		String category = request.getParameter("category");
		if (category == null)
			category = DEFAULT_CATEGORY;
		String pageNum = request.getParameter("page");
		if (pageNum == null)
			pageNum = "1";
		Map<String, Object> model = new HashMap<>();
		model.put("category", category);
		model.put("page", pageNum);
		model.put("popularApps", service.findMostPopular().iterator());
		Page apps = service.find(category, Integer.parseInt(pageNum));
		model.put("apps", apps.iterator());
		// ToDo: apps.getTotalElements and delete service.pageCount
		// or service.pageCount and delete apps
		model.put("pageCount", service.pageCount(category));
		
		return new ModelAndView("home", model);
	}
	
}
