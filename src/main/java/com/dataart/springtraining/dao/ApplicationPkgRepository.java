package com.dataart.springtraining.dao;

import com.dataart.springtraining.domain.ApplicationPkg;
import java.io.Serializable;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationPkgRepository extends CrudRepository<ApplicationPkg, Integer>
{
	public ApplicationPkg findByPkgName(String name);
}
