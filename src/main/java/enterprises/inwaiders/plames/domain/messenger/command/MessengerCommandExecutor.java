package enterprises.inwaiders.plames.domain.messenger.command;

import enterprises.inwaiders.plames.api.command.CommandException;
import enterprises.inwaiders.plames.api.command.CommandRegistry;
import enterprises.inwaiders.plames.api.locale.PlamesLocale;
import enterprises.inwaiders.plames.api.messenger.profile.UserProfile;

public class MessengerCommandExecutor {

	public static void execute(UserProfile profile, String rawText) throws CommandException {
		
		if(profile.getUser() == null) {
			
			throw new CommandException(PlamesLocale.getSystemMessage("profile.not_connected"));
		}
		
		String[] words = rawText.split(" ");
		
		String commandName = words[0].substring(1);		
		
		MessengerCommand command = getTargetCommand(words);
	
		int commandIndex = getTargetCommandIndex(words);
		
		String[] args = new String[words.length-(commandIndex+1)];
		
		System.arraycopy(words, commandIndex+1, args, 0, args.length);

		if(command != null) {
			
			command.run(profile, args);
		}
		else {
			
			throw new CommandException(PlamesLocale.getSystemMessage("command.not_found", words[commandIndex]));
		}
	}
	
	private static MessengerCommand getTargetCommand(String[] words) {
		
		MessengerCommand command = (MessengerCommand) CommandRegistry.getDefaultRegistry().getCommand(words[0]);
				
		if(words.length == 1) {
			
			return command;
		}
		
		for(int i = 1;i<words.length;i++) {
		
			String word = words[i];
		
			if(word.charAt(0) == '/') {
				
				command = command.getChildCommand(word);
				
				if(command == null) {
					
					return null;
				}
			}
		}
		
		return command;
	}
	
	private static int getTargetCommandIndex(String[] words) {
		
		for(int i = 0;i<words.length;i++) {
			
			if(words[i].charAt(0) != '/') {
				
				return i-1;
			}
		}
		
		return words.length-1;
	}
}
