package com.inwaiders.plames.system.event;

import com.inwaiders.plames.api.event.EventEngine;
import com.inwaiders.plames.api.event.EventEngineFactory;

public class EventEngineFactoryImpl extends EventEngineFactory {

	public EventEngineFactoryImpl() {
	
		commonEngine = getEventEngineInstance();
	}
	
	@Override
	public EventEngine getEventEngineInstance() {
		
		return new EventEngineImpl();
	}
}
