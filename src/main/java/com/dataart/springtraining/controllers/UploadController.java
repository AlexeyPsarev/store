package com.dataart.springtraining.controllers;

import com.dataart.springtraining.domain.ApplicationPkg;
import com.dataart.springtraining.service.ApplicationPkgService;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.stereotype.Controller
public class UploadController
{
	@Resource
	private ApplicationPkgService service;
	
	@RequestMapping(value = "upload.htm", method = RequestMethod.GET)
	public ModelAndView getUploadPage()
	{
		return new ModelAndView("upload");
	}
	
	@RequestMapping(value = "doUpload.htm", method = RequestMethod.POST)
	public ModelAndView handleFileUpload(
		@RequestParam("appName") String appName,
		@RequestParam("pkg") MultipartFile pkg,
		@RequestParam("category") String category,
		@RequestParam("description") String description,
		HttpSession session
	) throws IOException, ServletException
	{
		Integer author = (Integer)session.getAttribute("userId");
		Map<String, Object> model = new HashMap<>();
		if (appName.isEmpty())
			model.put(NAME_ERR_KEY, EMPTY_NAME);
		if (pkg.getSize() <= 0)
			model.put(FILE_ERR_KEY, EMPTY_FILE);
		if (!model.isEmpty())
		{
			model.put(APP_NAME_KEY, appName);
			return new ModelAndView("upload", model);
		}
		
		ApplicationPkg item = new ApplicationPkg();
		ApplicationPkgService.Status st = service.processZipFile(pkg, item, appName, category, description, author);
		if (st != ApplicationPkgService.Status.OK)
			return new ModelAndView("upload", FILE_ERR_KEY, st.getMessage());
		service.upload(pkg, item);
		
		return new ModelAndView("uploadDone");
	}

	/*@RequestMapping(value = "uploadDone.htm", method = RequestMethod.GET)
	public ModelAndView finishUpload(@RequestParam(value = "userId") Integer userId)
	{
		Map<String, Object> model = new HashMap<>();
		model.put("userId", userId);
		return new ModelAndView("home", model);
	}*/
	
	private static final String NAME_ERR_KEY = "appNameErr";
	private static final String FILE_ERR_KEY = "appFileErr";
	private static final String APP_NAME_KEY = "appName";
	
	private static final String EMPTY_NAME = "Application name is required";
	private static final String EMPTY_FILE = "Application file is required";
}
