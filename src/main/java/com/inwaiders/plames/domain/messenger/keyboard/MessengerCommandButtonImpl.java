package com.inwaiders.plames.domain.messenger.keyboard;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.inwaiders.plames.CoreModule;
import com.inwaiders.plames.api.command.CommandException;
import com.inwaiders.plames.api.messenger.keyboard.KeyboardCommandButton;
import com.inwaiders.plames.api.messenger.profile.UserProfile;
import com.inwaiders.plames.domain.messenger.command.MessengerCommandExecutor;
import com.inwaiders.plames.system.utils.MessageUtils;

public class MessengerCommandButtonImpl extends MessengerButtonBase implements KeyboardCommandButton {

	private String command = null;
	
	@Override
	public void action(UserProfile profile) {
	
		try {
			
			MessengerCommandExecutor.execute(profile, command);
		}
		catch(CommandException e) {
			
			MessageUtils.send(CoreModule.getSystemProfile(), profile, e.getMessage());
		}
	}

	public ObjectNode toJson() {
		
		ObjectNode node = super.toJson();
			node.put("type", "command");
			node.put("command", command);
	
		return node;
	}
	
	public void loadFromJson(ObjectNode node) {
		super.loadFromJson(node);
		
		this.command = node.get("command").asText();
	}
	
	@Override
	public void setCommand(String command) {
	
		this.command = command;
	}

	@Override
	public String getCommand() {
		
		return command;
	}
}
