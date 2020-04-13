package enterprises.inwaiders.plames.domain.messenger.command;

import java.util.HashSet;
import java.util.Set;

import enterprises.inwaiders.plames.api.command.CommandException;
import enterprises.inwaiders.plames.api.messenger.profile.UserProfile;
import enterprises.inwaiders.plames.domain.command.impl.CommandImpl;

public abstract class MessengerCommand extends CommandImpl {

	private Set<MessengerCommand> childCommands = new HashSet<>();
	
	public abstract void run(UserProfile profile, String... args) throws CommandException;

	public void addChildCommand(MessengerCommand command) {
		
		childCommands.add(command);
	}
	
	public MessengerCommand getChildCommand(String word) {
		
		if(word.charAt(0) == '/') {
			
			word = word.substring(1);
		}
		
		for(MessengerCommand command : childCommands) {
			
			if(command.getAliases().contains(word)) {
				
				return command;
			}
		}
		
		return null;
	}
	
	public Set<MessengerCommand> getChildCommands() {
		
		return childCommands;
	}
}
