package com.inwaiders.plames;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inwaiders.plames.api.command.CommandRegistry;
import com.inwaiders.plames.api.event.EventEngineFactory;
import com.inwaiders.plames.api.locale.PlamesLocale;
import com.inwaiders.plames.api.module.Module;
import com.inwaiders.plames.api.module.ModuleRegistry;
import com.inwaiders.plames.api.module.additions.ModulesContainer;
import com.inwaiders.plames.dao.locale.PlamesLocaleHlRepository;
import com.inwaiders.plames.dao.messenger.keyboard.KeyboardHlRepository;
import com.inwaiders.plames.dao.user.UserHlRepository;
import com.inwaiders.plames.domain.command.impl.CommandRegistryImpl;
import com.inwaiders.plames.domain.locale.PlamesLocaleImpl;
import com.inwaiders.plames.domain.messenger.command.impl.MessengerCommandPrimary;
import com.inwaiders.plames.domain.messenger.keyboard.MessengerKeyboardImpl;
import com.inwaiders.plames.domain.messenger.profile.commands.ProfileAcceptBindCommand;
import com.inwaiders.plames.domain.messenger.profile.commands.ProfileUnbindCommand;
import com.inwaiders.plames.domain.messenger.profile.impl.SystemProfile;
import com.inwaiders.plames.domain.module.impl.ModuleBase;
import com.inwaiders.plames.domain.user.impl.UserImpl;
import com.inwaiders.plames.system.event.EventEngineFactoryImpl;

public class CoreModule extends ModuleBase {

	private static final CoreModule INSTANCE = new CoreModule();

	public static CoreModuleConfiguration CONFIG = null;
	
	public static Logger LOGGER = LoggerFactory.getLogger(CoreModule.class);
	
	@Override
	public void preInit() {
		
		ModulesContainer modules = ModuleRegistry.getModules();
		
		for(Module module : modules) {
			
			if(module instanceof ModuleBase) {
				
				ModuleBase moduleBase = (ModuleBase) module;
				
				SystemProfile systemProfile = SystemProfile.getByModuleId(moduleBase.getId());
			
				if(systemProfile == null) {
					
					LOGGER.info("System profile for module "+moduleBase.getName()+" not found, will be created!");
					systemProfile = moduleBase.createProfile();
				}
				
				moduleBase.setProfile(systemProfile);
			}
		}
		
		//
		EventEngineFactory.setMainFactory(new EventEngineFactoryImpl());
		
		CommandRegistry.setDefaultRegistry(new CommandRegistryImpl());
		
		PlamesLocaleHlRepository.setRepository(new PlamesLocaleImpl.HighLevelRepository());
		PlamesLocaleImpl.loadBundles(new File(CONFIG.getBundlesRootDir()));
		PlamesLocaleImpl.initSystemLocale();
		
		KeyboardHlRepository.setRepository(new MessengerKeyboardImpl.HighLevelRepository());
		UserHlRepository.setRepository(new UserImpl.HighLevelRepository());
		
		CommandRegistry registry = CommandRegistry.getDefaultRegistry();
			registry.registerCommand(new MessengerCommandPrimary());
			registry.registerCommand(new ProfileUnbindCommand());
			registry.registerCommand(new ProfileAcceptBindCommand());
	}
	
	@Override
	public void init() {
	
	}
	
	@Override
	public String getDescription() {
		
		return PlamesLocale.getSystemMessage("module.core.description");
	}

	@Override
	public String getName() {
		
		return "Core";
	}

	@Override
	public String getLicenseKey() {
		
		return "";
	}

	@Override
	public long getId() {
		
		return 0;
	}

	@Override
	public String getType() {
		
		return "core";
	}

	@Override
	public String getVersion() {
	
		return "1V";
	}

	@Override
	public long getSystemVersion() {

		return 0;
	}
	
	public static SystemProfile getSystemProfile() {
		
		return INSTANCE.getProfile();
	}
	
	public static CoreModule getInstance() {
		
		return INSTANCE;
	}
}
