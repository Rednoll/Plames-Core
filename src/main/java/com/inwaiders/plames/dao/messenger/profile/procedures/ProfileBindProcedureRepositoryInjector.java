package com.inwaiders.plames.dao.messenger.profile.procedures;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inwaiders.plames.domain.messenger.profile.procedures.ProfileBindProcedure;

@Service
public class ProfileBindProcedureRepositoryInjector {

	@Autowired
	private ProfileBindProcedureRepository repository;

	@PostConstruct
	public void inject() {
		
		ProfileBindProcedure.setRepository(repository);
	}
}