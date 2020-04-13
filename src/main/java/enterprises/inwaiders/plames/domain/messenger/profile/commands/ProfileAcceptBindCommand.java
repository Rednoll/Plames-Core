package enterprises.inwaiders.plames.domain.messenger.profile.commands;

import enterprises.inwaiders.plames.CoreModule;
import enterprises.inwaiders.plames.api.command.CommandException;
import enterprises.inwaiders.plames.api.messenger.profile.UserProfile;
import enterprises.inwaiders.plames.api.user.User;
import enterprises.inwaiders.plames.domain.messenger.command.MessengerCommand;
import enterprises.inwaiders.plames.domain.messenger.profile.procedures.ProfileBindProcedure;
import enterprises.inwaiders.plames.system.utils.MessageUtils;

public class ProfileAcceptBindCommand extends MessengerCommand {

	public ProfileAcceptBindCommand() {
	
		this.addAliases("bind");
	}
	
	@Override
	public void run(UserProfile profile, String... args) throws CommandException {
	
		User user = profile.getUser();
		
		if(args.length != 1) {
			
			throw new CommandException("$command.accept.code_nf", "1");
		}
		
		String code = args[0];
		
		ProfileBindProcedure procedure = ProfileBindProcedure.getByCode(code);
	
		if(procedure == null) {
			
			throw new CommandException("$command.accept.bind_nf", code);
		}
		
		boolean consumeResult = procedure.codeGenerator.consume(code);
		
		if(!consumeResult) {
		
			throw new CommandException("$command.accept.code_expired");
		}
		
		UserProfile profileToBind = procedure.getProfile();
			profileToBind.setUser(user);
		
		profileToBind.save();
		
		user.getProfilesContainer().addProfile(profile);
		user.save();
		
		procedure.delete();
		
		MessageUtils.send(CoreModule.getSystemProfile(), profile, "$command.accept.success", profileToBind.getHumanSign(), user.getNickname());
		MessageUtils.send(CoreModule.getSystemProfile(), profileToBind, "$command.accept.success_sub", user.getNickname());
	}
}
