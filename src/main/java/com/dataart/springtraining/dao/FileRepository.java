package com.dataart.springtraining.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import org.apache.commons.io.FilenameUtils;
import org.h2.store.fs.FileUtils;
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
	private String realPkgDirPath;
	private String realImgDirPath;
	
	private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");
	private static final String DEFAULT_IMG_128 = "default128.png";
	private static final String DEFAULT_IMG_512 = "default512.png";
	private static final int BUFFER_SIZE = 512;
	
	@PostConstruct
	public void createDirs()
	{
		bundle = ResourceBundle.getBundle("directories");
		realAppPath = context.getRealPath("");
		osImgDir = IS_WINDOWS ? bundle.getString("IMG_DIR").replace("/", "\\") : bundle.getString("IMG_DIR");
		osPkgDir = IS_WINDOWS ? bundle.getString("PKG_DIR").replace("/", "\\") : bundle.getString("PKG_DIR");
		realPkgDirPath = realAppPath + osPkgDir + File.separator;
		realImgDirPath = realAppPath + osImgDir + File.separator;
		commonContextPath = context.getContextPath() + bundle.getString("IMG_DIR") + "/";
				
		File f = new File(realAppPath + osPkgDir);
		f.mkdirs();
		f = new File(realAppPath + osImgDir);
		f.mkdirs();
		setDefaultImage(DEFAULT_IMG_128);
		setDefaultImage(DEFAULT_IMG_512);
	}
	
	public String saveImage(String pkgName, String baseName, byte[] data) throws IOException
	{
		String dirName = FilenameUtils.removeExtension(pkgName);
		String absDirPath = realImgDirPath + dirName;
		Files.createDirectories(Paths.get(absDirPath));
		try (FileOutputStream f = new FileOutputStream(absDirPath + File.separator + baseName)) {
			f.write(data);
		}
		return commonContextPath + dirName + "/" + baseName;
	}
	
	public void saveMultipartFile(MultipartFile file) throws IOException
	{
		String filename = realPkgDirPath + file.getOriginalFilename();
		file.transferTo(new File(filename));
	}
	
	public String getDefaultPicture128Path()
	{
		return commonContextPath + DEFAULT_IMG_128;
	}
	
	public String getDefaultPicture512Path()
	{
		return commonContextPath + DEFAULT_IMG_512;
	}
	
	public void download(String pkgName, OutputStream out) throws IOException
	{
		String path = realPkgDirPath + pkgName;
		try (InputStream in = new FileInputStream(path)) {
			byte[] buffer = new byte[BUFFER_SIZE];
			int len;
			while ((len = in.read(buffer)) != -1)
				out.write(buffer, 0, len);
		}
	}
	
	public boolean fileExists(String name)
	{
		return FileUtils.exists(realPkgDirPath + name);
	}
	
	public long getFileSize(String name)
	{
		return FileUtils.size(realPkgDirPath + name);
	}
	
	private void setDefaultImage(String filename)
	{
		File out = new File(realImgDirPath + filename);
		try {
			out.createNewFile();
			try (FileInputStream is  = (FileInputStream)context.getResourceAsStream("/WEB-INF/classes/images/" + filename);
				FileChannel source = is.getChannel();
				FileChannel dest = new FileOutputStream(out).getChannel())
			{
				dest.transferFrom(source, 0, source.size());
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
