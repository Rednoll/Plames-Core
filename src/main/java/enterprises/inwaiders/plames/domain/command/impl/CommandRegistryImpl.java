package enterprises.inwaiders.plames.domain.command.impl;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import enterprises.inwaiders.plames.api.command.Command;
import enterprises.inwaiders.plames.api.command.CommandRegistry;

public class CommandRegistryImpl extends CommandRegistry {

	private static Logger logger = LoggerFactory.getLogger(CommandRegistry.class);
	
	private Set<Command> commands = new HashSet<>();
	private Set<String> allAliases = new HashSet<>();

	public void registerCommand(Command command) {
		
		Set<String> aliases = command.getAliases();
		
		for(String aliase : aliases) {
			
			if(allAliases.contains(aliase)) {
				
				throw new RuntimeException("Aliase collision for "+command.getClass().getSimpleName()+" aliase \""+aliase+"\"");
			}
		}
		
		allAliases.addAll(aliases);
		
		commands.add(command);
	
		logger.info("Successful register command: "+command.getClass().getSimpleName());
	}
	
	public Command getCommand(String aliase) {
		
		if(aliase.charAt(0) == '/') {
			
			aliase = aliase.substring(1);
		}
		
		for(Command suspect : commands) {
			
			if(suspect.getAliases().contains(aliase)) {
				
				return suspect;
			}
		}
		
		return null;
	}
	
	public Set<Command> getAll() {
	
		return commands;
	}
}
