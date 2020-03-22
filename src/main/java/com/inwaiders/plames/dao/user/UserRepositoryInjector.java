package com.inwaiders.plames.dao.user;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inwaiders.plames.domain.user.impl.UserImpl;

@Service
public class UserRepositoryInjector {

	@Autowired
	private UserRepository repository;
	
	@PostConstruct
	private void inject() {
		
		UserImpl.setRepository(repository);
	}
}
