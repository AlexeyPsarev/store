package com.dataart.springtraining.controllers;

import com.dataart.springtraining.domain.ApplicationPkg;
import com.dataart.springtraining.service.ApplicationPkgService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DownloadController
{
	@Resource
	private ApplicationPkgService appService;
	
	private static final String CONTENT_TYPE = "application/octet-stream";
	
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public void download(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		int id = Integer.parseInt(request.getParameter("app"));
		try (OutputStream outStream = response.getOutputStream()) {
			ApplicationPkg pkg = appService.findById(id);
			int size = (int)appService.getFileSize(pkg);
			response.setContentType(CONTENT_TYPE);
			response.setContentLength(size);
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", pkg.getPkgName()));
			appService.download(pkg, outStream);
		} catch (FileNotFoundException e) {
			// file doesn't exist
		}
	}
}
