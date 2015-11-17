package com.dataart.springtraining.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class FileRepository
{
	@Autowired
	private ServletContext context;
	
	private String realAppPath;
	private String commonContextPath;
	private ResourceBundle bundle;
	
	private static final String DEFAULT_IMG_128 = "default128";
	private static final String DEFAULT_IMG_512 = "default512";
	
	@PostConstruct
	public void createDir()
	{
		realAppPath = context.getRealPath("/");
		bundle = ResourceBundle.getBundle("directories");
		commonContextPath = context.getContextPath() + bundle.getString("IMG_DIR") + "/";
		File f = new File(realAppPath + bundle.getString("PKG_DIR"));
		f.mkdirs();
		f = new File(realAppPath + bundle.getString("IMG_DIR"));
		f.mkdirs();
		// ToDo:
		// upload default pictures
	}
	
	public String saveImage(String pkgName, String baseName, byte[] data) throws IOException
	{
		String dirName = FilenameUtils.removeExtension(pkgName);
		String relDirPath = bundle.getString("IMG_DIR") + File.separator + dirName;
		String absDirPath = realAppPath + relDirPath;
		Files.createDirectories(Paths.get(absDirPath));
		try (FileOutputStream f = new FileOutputStream(absDirPath + File.separator + baseName)) {
			f.write(data);
		}
		return commonContextPath + dirName + "/" + baseName;
	}
	
	public void saveMultipartFile(MultipartFile file) throws IOException
	{
		String filename = new StringBuilder(realAppPath).append(bundle.getString("PKG_DIR")).append(File.separator).
			append(file.getOriginalFilename()).toString();
		file.transferTo(new File(filename));
	}
	
	public String getDefaultPicture128Path(String pkgName)
	{
		return commonContextPath + DEFAULT_IMG_128;
	}
	
	public String getDefaultPicture512Path(String pkgName)
	{
		return commonContextPath + DEFAULT_IMG_512;
	}
}
