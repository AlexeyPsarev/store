package com.dataart.springtraining.controllers;

import com.dataart.springtraining.auxiliary.HeaderSetter;
import com.dataart.springtraining.domain.ApplicationPkg;
import com.dataart.springtraining.service.ApplicationPkgService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.stereotype.Controller
public class UploadController
{
	@Resource
	private ApplicationPkgService service;
		
	private static final String APP_NAME_KEY = "appName";
	private static final String PACKAGE_KEY = "pkg";
	private static final String CATEGORY_KEY = "category";
	private static final String DESCR_KEY = "description";
	
	private static final String NAME_ERR_KEY = "appNameErr";
	private static final String FILE_ERR_KEY = "appFileErr";
	
	private static final String EMPTY_NAME = "Application name is required";
	private static final String EMPTY_FILE = "Application file is required";
	
	@RequestMapping(value = "/upload.htm", method = RequestMethod.GET)
	public ModelAndView getUploadPage(HttpServletRequest request, HttpServletResponse response)
	{
		if (request.getSession(false).getAttribute("userId") == null)
			return new ModelAndView("redirect:/");
		HeaderSetter.setHeaders(response);
		return new ModelAndView("upload");
	}
	
	@RequestMapping(value = "/doUpload.htm", method = RequestMethod.POST)
	public ModelAndView handleFileUpload(
		@RequestParam(APP_NAME_KEY) String appName,
		@RequestParam(PACKAGE_KEY) MultipartFile pkg,
		@RequestParam(CATEGORY_KEY) String category,
		@RequestParam(DESCR_KEY) String description,
		HttpSession session,
		HttpServletResponse response
	) throws IOException
	{
		Integer author = (Integer)session.getAttribute("userId");
		if (author == null)
			return new ModelAndView("redirect:/");
		HeaderSetter.setHeaders(response);
		
		Map<String, Object> model = new HashMap<>();
		if (appName.isEmpty())
			model.put(NAME_ERR_KEY, EMPTY_NAME);
		if (pkg.getSize() <= 0)
			model.put(FILE_ERR_KEY, EMPTY_FILE);
		if (!model.isEmpty())
		{
			model.put(APP_NAME_KEY, appName);
			model.put(DESCR_KEY, description);
			return new ModelAndView("upload", model);
		}
		
		ApplicationPkg item = new ApplicationPkg();
		ApplicationPkgService.Status st = service.processZipFile(pkg, item, appName, category, description, author);
		if (st != ApplicationPkgService.Status.OK)
			return new ModelAndView("upload", FILE_ERR_KEY, st.getMessage());
		service.upload(pkg, item);
		return new ModelAndView("uploadDone");
	}
}
