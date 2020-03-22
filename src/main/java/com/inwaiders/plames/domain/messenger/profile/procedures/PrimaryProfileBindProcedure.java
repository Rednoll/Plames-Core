package com.inwaiders.plames.domain.messenger.profile.procedures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inwaiders.plames.CoreModule;
import com.inwaiders.plames.api.messenger.profile.UserProfile;
import com.inwaiders.plames.api.messenger.profile.additions.ProfilesContainer;
import com.inwaiders.plames.api.procedure.ProcedureStage;
import com.inwaiders.plames.api.procedure.ProcedureStageRunResult;
import com.inwaiders.plames.api.procedure.ProcedureStageStatus;
import com.inwaiders.plames.api.user.User;
import com.inwaiders.plames.system.utils.MessageUtils;

@Entity
@Table(name = "primary_profile_bind_procedures")
public class PrimaryProfileBindProcedure extends ProfileProcedure<ProcedureStage<PrimaryProfileBindProcedure>>{
	
	@Column(name = "picked_messenger_type")
	private String pickedMessengerType = null;
	
	@Transient
	private List<UserProfile> profiles = null;
	
	public PrimaryProfileBindProcedure(UserProfile profile, String pickedMessengerType) {
		this();
		
		this.profile = profile;
		this.pickedMessengerType = pickedMessengerType;
	}
	
	private PrimaryProfileBindProcedure() {
		
		this.getStages().add(new ProcedureStage<PrimaryProfileBindProcedure>() {
			
			@Override
			public ProcedureStageRunResult run(PrimaryProfileBindProcedure procedure, String... args) {
				
				String rawNumber = args[0];
				
				User user = procedure.profile.getUser();
				
				List<UserProfile> profiles = procedure.getProfiles();
				
				try {
					
					int number = Integer.valueOf(rawNumber);
				
					number -= 1; //-1 because for user list start from 1
					
					if(number < 0 || number >= profiles.size()) {
						
						MessageUtils.send(CoreModule.getSystemProfile(), procedure.profile, "$procedure.primary.index_oob", String.valueOf(rawNumber));
						return new ProcedureStageRunResult(ProcedureStageStatus.REPEAT);
					}
					else {
						
						UserProfile newPrimaryProfile = profiles.get(number);
					
						user.getProfilesContainer().setPrimaryProfile(newPrimaryProfile);
						
						user.save();
						
						MessageUtils.send(CoreModule.getSystemProfile(), procedure.profile, "$command.primary.success", newPrimaryProfile.getHumanSign());
						
						return new ProcedureStageRunResult(ProcedureStageStatus.COMPLETE);
					}
				}
				catch(NumberFormatException e) {
				
					MessageUtils.send(CoreModule.getSystemProfile(), procedure.profile, "$procedure.primary.index_er", String.valueOf(rawNumber));
					return new ProcedureStageRunResult(ProcedureStageStatus.REPEAT);
				}
			}
		});
	}
	
	@Override
	public void onBegin() {
		super.onBegin();
		
		showProfiles();
	}
	
	@Override
	public void onComplete() {
		super.onComplete();

	}
	
	private void showProfiles() {
		
		List<UserProfile> profiles = getProfiles();
		
		StringBuilder builder = new StringBuilder();
			builder.append(profile.getUser().getLocale().getMessage("procedure.primary.begin_info"));
			
			for(int i = 0; i<profiles.size(); i++) {
				
				builder.append("\n"+(i+1)+". "+profiles.get(i).getHumanSign());
			}
			
		MessageUtils.send(CoreModule.getSystemProfile(), profile, builder.toString());
	}
	
	private List<UserProfile> getProfiles() {
	
		if(this.profiles == null) {
			
			ProfilesContainer profilesContainer = getProfile().getUser().getProfilesContainer();
		
			this.profiles = new ArrayList<>(profilesContainer.getProfiles(this.pickedMessengerType));
			
			Collections.sort(this.profiles, (UserProfile o1, UserProfile o2) -> o1.getId().compareTo(o2.getId()));
		}
		
		return this.profiles;
	}
	
	public static PrimaryProfileBindProcedure create(UserProfile profile, String pickedMessengerType) {
		
		PrimaryProfileBindProcedure proc = new PrimaryProfileBindProcedure(profile, pickedMessengerType);
		
		proc = repository.save(proc);
		
		return proc;
	}
}
