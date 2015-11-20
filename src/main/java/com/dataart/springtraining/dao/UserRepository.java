package com.dataart.springtraining.dao;

import com.dataart.springtraining.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer>
{
	public User findByUsername(String username);
}
