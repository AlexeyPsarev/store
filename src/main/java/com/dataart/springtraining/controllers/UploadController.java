package com.dataart.springtraining.controllers;

import com.dataart.springtraining.domain.ApplicationPkg;
import com.dataart.springtraining.service.ApplicationPkgService;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
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
	
	@RequestMapping(value = "upload.htm", method = RequestMethod.GET)
	public ModelAndView getUploadPage(@RequestParam("userId") Integer userId)
	{
		Map<String, Object> model = new HashMap<>();
		model.put("userId", userId);
		return new ModelAndView("upload", model);
	}
	
	@RequestMapping(value = "doUpload.htm", method = RequestMethod.POST)
	public ModelAndView handleFileUpload(
		@RequestParam("appName") String appName,
		@RequestParam("pkg") MultipartFile pkg,
		@RequestParam("category") String category,
		@RequestParam("description") String description,
		@RequestParam("userId") Integer author
	) throws IOException, ServletException
	{
		Map<String, Object> model = new HashMap<>();
		model.put("userId", author);
		if (appName.isEmpty())
			model.put(NAME_ERR_KEY, EMPTY_NAME);
		if (pkg.getSize() <= 0)
			model.put(FILE_ERR_KEY, EMPTY_FILE);
		if (model.size() > 1)
		{
			model.put(APP_NAME_KEY, appName);
			return new ModelAndView("upload", model);
		}
		
		ApplicationPkg item = new ApplicationPkg();
		try (ZipInputStream archive = new ZipInputStream(pkg.getInputStream()))
		{
			Map<String, byte[]> entries = new HashMap<>();
			ZipEntry entry;
			StringBuilder sbPic128 = new StringBuilder();
			StringBuilder sbPic512 = new StringBuilder();
			while ((entry = archive.getNextEntry()) != null)
			{
				String entryName = entry.getName();
				if (entryName.endsWith(".txt"))
				{
					if (!processTextFile(archive, item, model, appName, pkg.getOriginalFilename(), sbPic128, sbPic512))
						return new ModelAndView("upload", model);
				}
				else
				{
					if (!processBinaryFile(archive, entryName, entries, model))
						return new ModelAndView("upload", model);
				}
			}
			try {
				String strPic128 = sbPic128.toString();
				String strPic512 = sbPic512.toString();
				if (!strPic128.isEmpty())
					item.setPic128(new SerialBlob(entries.get(strPic128)));
				if (!strPic512.isEmpty())
					item.setPic512(new SerialBlob(entries.get(strPic512)));
			} catch (SQLException e) {
				// ToDo: сделать непустой catch
				item.setPic128(null);
				item.setPic512(null);
			}
		} catch (IOException e) {
			model.put(FILE_ERR_KEY, PARSING_ERR);
			return new ModelAndView("upload", model);
		}
		
		item.setCategory(category);
		item.setDownloads(0);
		item.setTimeUploaded(Timestamp.from(ZonedDateTime.now(ZoneId.of(TIMEZONE)).toInstant()));
		item.setDescription(description);
		item.setAuthor(author);
		service.upload(item);
		pkg.transferTo(new File(System.getenv("CATALINA_HOME") + "/bin/packages/" + pkg.getOriginalFilename()));
		return new ModelAndView("uploadDone", model);
	}

	@RequestMapping(value = "uploadDone.htm", method = RequestMethod.GET)
	public ModelAndView finishUpload(@RequestParam(value = "userId") Integer userId)
	{
		Map<String, Object> model = new HashMap<>();
		model.put("userId", userId);
		return new ModelAndView("home", model);
	}
	
	private boolean processTextFile(ZipInputStream input, ApplicationPkg item, Map<String, Object> model,
		String appName, String pkgName, StringBuilder pic128, StringBuilder pic512) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(input)));
		String line;
		pic128.delete(0, pic128.length());
		pic512.delete(0, pic512.length());
		while ((line = reader.readLine()) != null)
		{
			if (line.startsWith(NAME))
			{
				String parsedAppName = line.substring(NAME.length());
				if (parsedAppName.isEmpty())
				{
					model.put(FILE_ERR_KEY, EMPTY_APP_NAME);
					return false;
				}
				if (!parsedAppName.equals(appName))
				{
					model.put(FILE_ERR_KEY, BAD_APP_NAME);
					return false;
				}
				item.setAppName(parsedAppName);
			}
			else if (line.startsWith(PACKAGE))
			{
				String parsedPkgName = line.substring(PACKAGE.length());
				if (parsedPkgName.isEmpty())
				{
					model.put(FILE_ERR_KEY, EMPTY_PKG_NAME);
					return false;
				}
				if (!parsedPkgName.equals(pkgName))
				{
					model.put(FILE_ERR_KEY, BAD_PKG_NAME);
					return false;
				}
				if (service.pkgExists(parsedPkgName))
				{
					model.put(FILE_ERR_KEY, PKG_EXISTS);
					return false;
				}
				item.setPkgName(parsedPkgName);
			}
			else if (line.startsWith(PIC128))
				pic128.append(line.substring(PIC128.length()));
			else if (line.startsWith(PIC512))
				pic512.append(line.substring(PIC512.length()));
		}
		return true;
	}
	
	private boolean processBinaryFile(ZipInputStream input, String entryName,
		Map<String, byte[]> entries, Map<String, Object> model)
	{
		byte[] buf = new byte[BUFFER_SIZE];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			int len;
			while ((len = input.read(buf)) != -1)
			{
				bos.write(buf, 0, len);
			}
		} catch (IOException e) {
			model.put(FILE_ERR_KEY, PARSING_ERR);
			return false;
		}
		entries.put(entryName, bos.toByteArray());
		return true;
	}
	
	private static final String NAME_ERR_KEY = "appNameErr";
	private static final String FILE_ERR_KEY = "appFileErr";
	private static final String APP_NAME_KEY = "appName";
	
	private static final String EMPTY_NAME = "Application name is required";
	private static final String EMPTY_FILE = "Application file is required";
	
	private static final String EMPTY_PKG_NAME = "The package name is empty";
	private static final String BAD_PKG_NAME = "The package name is incorrect";
	private static final String EMPTY_APP_NAME = "The application name is empty";
	private static final String BAD_APP_NAME = "The application name is incorrect";
	private static final String PKG_EXISTS = "Cannot upload file. The package already exists";
	private static final String PARSING_ERR = "Cannot upload file. The package is corrupted";
	
	private static final String NAME = "name:";
	private static final String PACKAGE = "package:";
	private static final String PIC128 = "picture_128:";
	private static final String PIC512 = "picture_512:";
	
	private static final String TIMEZONE = "GMT+3";
	private static final int BUFFER_SIZE = 512;
}
