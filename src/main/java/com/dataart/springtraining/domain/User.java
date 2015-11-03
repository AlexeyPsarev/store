package com.dataart.springtraining.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User
{

	public User() 
	{
	}

	public User(String username, String password, String salt, String fullname)
	{
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.fullname = fullname;
	}
	
	public Integer getId()
	{
		return id;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public String getSalt()
	{
		return salt;
	}
	
	public String getFullname()
	{
		return fullname;
	}
	
	public void setId(Integer id)
	{
		this.id = id;
	}
	
	public void setUsername(String value)
	{
		username = value;
	}
	
	public void setPassword(String value)
	{
		password = value;
	}
	
	public void setSalt(String value)
	{
		salt = value;
	}
	
	public void setFullname(String value)
	{
		fullname = value;
	}
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "salt")
	private String salt;
	
	@Column(name = "full_name")
	private String fullname;
		
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    //@SequenceGenerator(name = "id_gen", sequenceName = "id_seq", initialValue = 1, allocationSize = 1)
	
}
