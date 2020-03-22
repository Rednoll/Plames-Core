package com.inwaiders.plames.spring;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inwaiders.plames.CoreModule;
import com.inwaiders.plames.CoreModuleConfiguration;

@Service
public class CoreModuleConfigurationAccessor {

	@Autowired
	private CoreModuleConfiguration mainConfig;
	
	public static CoreModuleConfiguration CONFIG = null;

	@PostConstruct
	private void statize() {
		
		CONFIG = mainConfig;
		CoreModule.CONFIG = CONFIG;
	}
}