package com.dataart.springtraining.dao;

import com.dataart.springtraining.domain.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserRepository
{
	@PersistenceContext
	private EntityManager em;

	public void saveUser(User user)
	{
		em.merge(user);
	}
}
