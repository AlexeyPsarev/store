package com.dataart.springtraining.service;

import com.dataart.springtraining.dao.UserRepository;
import com.dataart.springtraining.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
	@Autowired
	private UserRepository dao;
	
	@Autowired
	private PasswordEncoder encoder;
	
	public boolean canCreate(User user)
	{
		return (dao.findByUsername(user.getUsername()) == null);
	}
	
	public void save(User user)
	{
		user = dao.save(user);
	}
	
	public boolean authenticate(String username, String rawPassword)
	{
		User correctUser = dao.findByUsername(username);
		return (correctUser != null && encoder.matches(rawPassword, correctUser.getPassword()));
	}
	
	public User find(String username)
	{
		return dao.findByUsername(username);
	}
	
	public User find(Integer id)
	{
		return dao.findOne(id);
	}
}
