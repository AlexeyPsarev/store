package com.dataart.springtraining.dao;

import com.dataart.springtraining.domain.User;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

//@Repository
//@Transactional
public interface UserRepository extends CrudRepository<User, Integer>
{
	//@PersistenceContext
	//private EntityManager em;
}
