package com.dataart.springtraining.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
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
	private String osImgDir;
	private String osPkgDir;
	
	private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");
	private static final String DEFAULT_IMG_128 = "default128";
	private static final String DEFAULT_IMG_512 = "default512";
	
	@PostConstruct
	public void createDir()
	{
		realAppPath = context.getRealPath("");
		bundle = ResourceBundle.getBundle("directories");
		osImgDir = IS_WINDOWS ? bundle.getString("IMG_DIR").replace("/", "\\") : bundle.getString("IMG_DIR");
		osPkgDir = IS_WINDOWS ? bundle.getString("PKG_DIR").replace("/", "\\") : bundle.getString("PKG_DIR");
		commonContextPath = context.getContextPath() + bundle.getString("IMG_DIR") + "/";
		
		File f = new File(realAppPath + osPkgDir);
		f.mkdirs();
		f = new File(realAppPath + osImgDir);
		f.mkdirs();
		// ToDo:
		// upload default pictures
	}
	
	public String saveImage(String pkgName, String baseName, byte[] data) throws IOException
	{
		String dirName = FilenameUtils.removeExtension(pkgName);
		String relDirPath = osImgDir + File.separator + dirName;
		String absDirPath = realAppPath + relDirPath;
		Files.createDirectories(Paths.get(absDirPath));
		try (FileOutputStream f = new FileOutputStream(absDirPath + File.separator + baseName)) {
			f.write(data);
		}
		return commonContextPath + dirName + "/" + baseName;
	}
	
	public void saveMultipartFile(MultipartFile file) throws IOException
	{
		String filename = new StringBuilder(realAppPath).append(osPkgDir).append(File.separator).
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
