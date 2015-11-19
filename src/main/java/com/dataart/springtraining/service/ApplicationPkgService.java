package com.dataart.springtraining.service;

import com.dataart.springtraining.dao.ApplicationPkgRepository;
import com.dataart.springtraining.dao.FileRepository;
import com.dataart.springtraining.domain.ApplicationPkg;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.ServletContext;
import javax.sql.rowset.serial.SerialBlob;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Service
public class ApplicationPkgService
{
	@Autowired
	private ApplicationPkgRepository appRepository;
	
	@Autowired
	private FileRepository fileRepository;
	
	public enum Status
	{
		OK("OK"),
		EMPTY_PKG_NAME("The package name is empty"),
		BAD_PKG_NAME("The package name is incorrect"),
		EMPTY_APP_NAME("The application name is empty"),
		BAD_APP_NAME("The application name is incorrect"),
		PKG_EXISTS("Cannot upload file. The package already exists"),
		PARSING_ERR("Cannot upload file. The package is corrupted");
		
		private final String MSG;
		
		private Status(String msg)
		{
			MSG = msg;
		}
		
		public String getMessage()
		{
			return MSG;
		}
	}
	
	public void upload(MultipartFile file, ApplicationPkg app) throws IOException
	{
		fileRepository.saveMultipartFile(file);
		appRepository.save(app);
	}
	
	public Status processZipFile(MultipartFile pkg, ApplicationPkg item, String appName, String category, String description, Integer author)
	{
		try (ZipInputStream archive = new ZipInputStream(pkg.getInputStream()))
		{
			Map<String, byte[]> entries = new HashMap<>();
			ZipEntry entry;
			String pkgName = pkg.getOriginalFilename();
			StringBuilder sbPic128 = new StringBuilder();
			StringBuilder sbPic512 = new StringBuilder();
			while ((entry = archive.getNextEntry()) != null)
			{
				String entryName = entry.getName();
				Status curStatus;
				if (entryName.endsWith(".txt"))
				{
					curStatus = processTextFile(archive, item, appName, pkgName, sbPic128, sbPic512);
					if (curStatus != Status.OK)
						return curStatus;
				}
				else
				{
					curStatus = processBinaryFile(archive, entryName, entries);
					if (curStatus != Status.OK)
						return curStatus;
				}
			}

			try {
				String pic128 = sbPic128.toString();
				item.setPic128(fileRepository.saveImage(pkgName, pic128, entries.get(pic128)));
			} catch (IOException | NullPointerException e) {
				item.setPic128(fileRepository.getDefaultPicture128Path());
			}
			
			try {
				String pic512 = sbPic512.toString();
				item.setPic512(fileRepository.saveImage(pkgName, pic512, entries.get(pic512)));
			} catch (IOException | NullPointerException e) {
				item.setPic512(fileRepository.getDefaultPicture512Path());
			}
			
		} catch (IOException e) {
			return Status.PARSING_ERR;
		}
		item.setCategory(category);
		item.setDownloads(0);
		item.setTimeUploaded(Timestamp.from(ZonedDateTime.now(ZoneId.of(TIMEZONE)).toInstant()));
		item.setDescription(description);
		item.setAuthor(author);
		return Status.OK;
	}
	
	public Page<ApplicationPkg> findMostPopular()
	{
		PageRequest req = new PageRequest(0, POPULAR_APP_COUNT, new Sort(Sort.Direction.DESC, "downloads", "timeUploaded"));
		return appRepository.findAll(req);
	}
	
	public Page<ApplicationPkg> find(String category, int page)
	{
		PageRequest request = new PageRequest(page - 1, PAGE_SIZE, Sort.Direction.DESC, "timeUploaded");
		return appRepository.findByCategory(category, request);
	}
	
	public Long pageCount(String category)
	{
		long appCount = appRepository.countByCategory(category);
		return (appCount % PAGE_SIZE == 0) ? (appCount / PAGE_SIZE) : (appCount / PAGE_SIZE + 1);
	}
	
	public ApplicationPkg findById(Integer id)
	{
		return appRepository.findOne(id);
	}
	
	private Status processTextFile(ZipInputStream input, ApplicationPkg item, String appName, String pkgName,
		StringBuilder pic128, StringBuilder pic512) throws IOException
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
					return Status.EMPTY_APP_NAME;
				if (!parsedAppName.equals(appName))
					return Status.BAD_APP_NAME;
				item.setAppName(parsedAppName);
			}
			else if (line.startsWith(PACKAGE))
			{
				String parsedPkgName = line.substring(PACKAGE.length());
				if (parsedPkgName.isEmpty())
					return Status.EMPTY_PKG_NAME;
				if (!parsedPkgName.equals(pkgName))
					return Status.BAD_PKG_NAME;
				if (pkgExists(parsedPkgName))
					return Status.PKG_EXISTS;
				item.setPkgName(parsedPkgName);
			}
			else if (line.startsWith(PIC128))
				pic128.append(line.substring(PIC128.length()));
			else if (line.startsWith(PIC512))
				pic512.append(line.substring(PIC512.length()));
		}
		return Status.OK;
	}
	
	public long getFileSize(ApplicationPkg pkg)
	{
		return fileRepository.getFileSize(pkg.getPkgName());
	}
	
	public int download(ApplicationPkg pkg, OutputStream out) throws FileNotFoundException, IOException
	{
		// pkg = findById(id);
		// syncronized ?
		// ToDo: check operator++
		pkg.setDownloads(pkg.getDownloads() + 1);
		appRepository.save(pkg);
		int size = fileRepository.download(pkg.getPkgName(), out);
		return size;
	}
	
	private Status processBinaryFile(ZipInputStream input, String entryName, Map<String, byte[]> entries)
	{
		byte[] buf = new byte[BUFFER_SIZE];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			int len;
			while ((len = input.read(buf)) != -1)
				bos.write(buf, 0, len);
		} catch (IOException e) {
			return Status.PARSING_ERR;
		}
		
		entries.put(entryName, bos.toByteArray());
		return Status.OK;
	}
	
	private boolean pkgExists(String name)
	{
		return (appRepository.findByPkgName(name) != null);
	}
	
	private static final String NAME = "name:";
	private static final String PACKAGE = "package:";
	private static final String PIC128 = "picture_128:";
	private static final String PIC512 = "picture_512:";
	
	private static final String TIMEZONE = "GMT+3";
	private static final int BUFFER_SIZE = 512;
	private static final int POPULAR_APP_COUNT = 5;
	private static final int PAGE_SIZE = 5;
}

