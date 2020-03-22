package com.inwaiders.plames.domain.messenger.profile.commands;

import com.inwaiders.plames.CoreModule;
import com.inwaiders.plames.api.command.CommandException;
import com.inwaiders.plames.api.messenger.profile.UserProfile;
import com.inwaiders.plames.domain.messenger.command.MessengerCommand;
import com.inwaiders.plames.system.utils.MessageUtils;

public class ProfileUnbindCommand extends MessengerCommand {

	public ProfileUnbindCommand() {
		
		this.addAliases("unbind");
	}
	
	@Override
	public void run(UserProfile profile, String... args) throws CommandException {
		
		MessageUtils.send(CoreModule.getSystemProfile(), profile, "$command.unbind.success", profile.getHumanSign());
		profile.delete();
	}
}
