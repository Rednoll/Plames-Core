package enterprises.inwaiders.plames.domain.messenger.profile.commands;

import enterprises.inwaiders.plames.CoreModule;
import enterprises.inwaiders.plames.api.command.CommandException;
import enterprises.inwaiders.plames.api.messenger.profile.UserProfile;
import enterprises.inwaiders.plames.domain.messenger.command.MessengerCommand;
import enterprises.inwaiders.plames.system.utils.MessageUtils;

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
