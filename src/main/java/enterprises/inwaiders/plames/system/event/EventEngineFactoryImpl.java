package enterprises.inwaiders.plames.system.event;

import enterprises.inwaiders.plames.api.event.EventEngine;
import enterprises.inwaiders.plames.api.event.EventEngineFactory;

public class EventEngineFactoryImpl extends EventEngineFactory {

	public EventEngineFactoryImpl() {
	
		commonEngine = getEventEngineInstance();
	}
	
	@Override
	public EventEngine getEventEngineInstance() {
		
		return new EventEngineImpl();
	}
}
