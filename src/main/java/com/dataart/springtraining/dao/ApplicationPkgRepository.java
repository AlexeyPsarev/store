package com.dataart.springtraining.dao;

import com.dataart.springtraining.domain.ApplicationPkg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ApplicationPkgRepository extends PagingAndSortingRepository<ApplicationPkg, Integer>
{
	public ApplicationPkg findByPkgName(String name);
	public Page<ApplicationPkg> findByCategory(String category, Pageable request);
	public Long countByCategory(String category);
}
