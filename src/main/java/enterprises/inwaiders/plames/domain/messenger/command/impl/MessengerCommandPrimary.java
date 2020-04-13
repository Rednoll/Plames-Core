package enterprises.inwaiders.plames.domain.messenger.command.impl;

import java.util.Set;

import enterprises.inwaiders.plames.CoreModule;
import enterprises.inwaiders.plames.api.command.CommandException;
import enterprises.inwaiders.plames.api.messenger.profile.UserProfile;
import enterprises.inwaiders.plames.api.messenger.profile.additions.ProfilesContainer;
import enterprises.inwaiders.plames.api.user.User;
import enterprises.inwaiders.plames.domain.messenger.command.MessengerCommand;
import enterprises.inwaiders.plames.domain.messenger.impl.MessengerImpl;
import enterprises.inwaiders.plames.domain.messenger.profile.procedures.PrimaryProfileBindProcedure;
import enterprises.inwaiders.plames.system.utils.MessageUtils;

public class MessengerCommandPrimary extends MessengerCommand{

	public MessengerCommandPrimary() {
		super();
		
		this.addAliases("primary");
	}
	
	@Override
	public void run(UserProfile profile, String... args) throws CommandException {
	
		if(profile.getCurrentProcedure() != null) throw new CommandException("$procedure.already_runned");
	
		User user = profile.getUser();
		ProfilesContainer profilesContainer = user.getProfilesContainer();
		
		if(args.length == 0) {
		
			profilesContainer.setPrimaryProfile(profile);
		
			user.save();
			
			MessageUtils.send(CoreModule.getSystemProfile(), profile, "$command.primary.success", profile.getHumanSign());
			return;
		}
		else if(args.length == 1) {
			
			String messengerType = args[0];
			
			if(MessengerImpl.getByType(messengerType) == null) {
				
				MessageUtils.send(CoreModule.getSystemProfile(), profile, "$messenger.not_found", messengerType);
				return;
			}
			
			Set<UserProfile> profiles = profilesContainer.getProfiles(messengerType);
		
			if(profiles.isEmpty()) {
				
				MessageUtils.send(CoreModule.getSystemProfile(), profile, "$profile.specific_nf", messengerType);
				return;
			}
			
			if(profiles.size() == 1) {
				
				UserProfile newPrimaryProfile = profiles.iterator().next();
			
				profilesContainer.setPrimaryProfile(newPrimaryProfile);
				
				user.save();
				
				MessageUtils.send(CoreModule.getSystemProfile(), profile, "$command.primary.success", newPrimaryProfile.getHumanSign());
				return;
			}
			else {
				
				PrimaryProfileBindProcedure proc = PrimaryProfileBindProcedure.create(profile, messengerType);
					proc.begin();
				
				proc.save();
				
				profile.setCurrentProcedure(proc);
				profile.save();		
				return;
			}
		}
	}
}
