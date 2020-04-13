package enterprises.inwaiders.plames.system.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import enterprises.inwaiders.plames.api.event.EventEngine;
import enterprises.inwaiders.plames.api.event.EventStage;
import enterprises.inwaiders.plames.api.event.PlamesEvent;
import enterprises.inwaiders.plames.api.event.PlamesHandler;

public class EventEngineImpl implements EventEngine {

	private Map<Class<? extends PlamesEvent>, Set<PlamesHandler<?>>> preStageHandlers = new HashMap<>();
	private Map<Class<? extends PlamesEvent>, Set<PlamesHandler<?>>> postStageHandlers = new HashMap<>();
	
	@Override
	public void run(PlamesEvent event, EventStage stage) {
		
		Set<PlamesHandler<?>> handlers = null;
		
		if(stage == EventStage.PRE) {
			
			handlers = preStageHandlers.get(event.getClass());
		}
		
		if(stage == EventStage.POST) {
			
			handlers = postStageHandlers.get(event.getClass());
		}
		
		if(handlers != null) {
			
			for(PlamesHandler handler : handlers) {
				
				handler.run(event);
			}
		}
		
		if(event.getAutoDispose()) {
			
			event.dispose();
		}
	}
	
	@Override
	public <E extends PlamesEvent> void register(Class<E> event, EventStage stage, PlamesHandler<E> handler) {
		
		Set<PlamesHandler<?>> handlers = null;
		
		if(stage == EventStage.PRE) {
			
			handlers = preStageHandlers.get(event);
			
			if(handlers == null) {
				
				handlers = new HashSet<>();
				
				preStageHandlers.put(event, handlers);
			}
		}
		else if(stage == EventStage.POST) {
			
			handlers = postStageHandlers.get(event);
			
			if(handlers == null) {
				
				handlers = new HashSet<>();
				
				postStageHandlers.put(event, handlers);
			}
		}
		
		handlers.add(handler);
	}
}
