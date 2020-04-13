package enterprises.inwaiders.plames.spring;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enterprises.inwaiders.plames.CoreModule;
import enterprises.inwaiders.plames.CoreModuleConfiguration;

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