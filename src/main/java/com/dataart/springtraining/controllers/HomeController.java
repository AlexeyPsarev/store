package com.dataart.springtraining.controllers;

import com.dataart.springtraining.auxiliary.HeaderSetter;
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
	private static final String CATEGORY_KEY = "category";
	private static final String PAGE_NUM_KEY = "page";
	private static final String POPULAR_KEY = "popularApps";
	private static final String PAGE_COUNT_KEY = "pageCount";
	private static final String APPLICATIONS_KEY = "apps";
		
	@Override
	@RequestMapping(value = "/home.htm", method = RequestMethod.GET)
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
	{
		if (request.getSession(false).getAttribute("userId") == null)
			return new ModelAndView("redirect:/");
		HeaderSetter.setHeaders(response);
		
		String category = request.getParameter(CATEGORY_KEY);
		if (category == null)
			category = DEFAULT_CATEGORY;
		String pageNum = request.getParameter(PAGE_NUM_KEY);
		if (pageNum == null)
			pageNum = "1";
		Map<String, Object> model = new HashMap<>();
		model.put(CATEGORY_KEY, category);
		model.put(PAGE_NUM_KEY, pageNum);
		model.put(POPULAR_KEY, service.findMostPopular().iterator());
		Page apps = service.find(category, Integer.parseInt(pageNum));
		model.put(APPLICATIONS_KEY, apps.iterator());
		model.put(PAGE_COUNT_KEY, service.pageCount(category));
		
		return new ModelAndView("home", model);
	}
}
