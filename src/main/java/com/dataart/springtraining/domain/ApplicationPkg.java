package com.dataart.springtraining.domain;

import java.sql.Blob;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "applications")
public class ApplicationPkg
{
	public ApplicationPkg()
	{
	}
	
	public void setId(Integer id)
	{
		this.id = id;
	}
	
	public void setAppName(String name)
	{
		this.appName = name;
	}
	
	public void setPkgName(String name)
	{
		this.pkgName = name;
	}
	
	public void setPic128(Blob pic)
	{
		this.pic128 = pic;
	}
	
	public void setPic512(Blob pic)
	{
		this.pic512 = pic;
	}
	
	public void setCategory(String category)
	{
		this.category = category;
	}
	
	public void setDownloads(Integer value)
	{
		this.downloads = value;
	}
	
	public void setTimeUploaded(Timestamp t)
	{
		this.timeUploaded = t;
	}
	
	public void setDescription(String d)
	{
		this.description = d;
	}
	
	public void setAuthor(Integer author)
	{
		this.author = author;
	}
	
	public Integer getId()
	{
		return id;
	}
	
	public String getAppName()
	{
		return appName;
	}
	
	public String getPkgName()
	{
		return pkgName;
	}
	
	public Blob getPic128()
	{
		return pic128;
	}
	
	public Blob getPic512()
	{
		return pic512;
	}
	
	public String getCategory()
	{
		return category;
	}
	
	public Integer getDownloads()
	{
		return downloads;
	}
	
	public Timestamp getTimeUploaded()
	{
		return timeUploaded;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public Integer getAuthor()
	{
		return author;
	}
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "app_name")
	private String appName;
	
	@Column(name = "pkg_name")
	private String pkgName;
	
	@Column(name = "picture_128")
	private Blob pic128;
	
	@Column(name = "picture_512")
	private Blob pic512;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "downloads")
	private Integer downloads;
	
	@Column(name = "time_uploaded")
	private Timestamp timeUploaded;
	
	@Column(name = "description")
	private String description;
	
	//@ManyToOne(targetEntity = User.class)
	//private User author;
	
	@Column(name = "author")
	private Integer author;
}
