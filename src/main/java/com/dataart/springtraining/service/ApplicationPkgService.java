package com.dataart.springtraining.service;

import com.dataart.springtraining.dao.ApplicationPkgRepository;
import com.dataart.springtraining.domain.ApplicationPkg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationPkgService
{
	@Autowired
	private ApplicationPkgRepository dao;
	
	public boolean pkgExists(String name)
	{
		return (dao.findByPkgName(name) != null);
	}
	
	public void upload(ApplicationPkg app)
	{
		dao.save(app);
	}
}

