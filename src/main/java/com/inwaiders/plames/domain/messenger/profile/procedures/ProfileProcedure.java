package com.inwaiders.plames.domain.messenger.profile.procedures;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.inwaiders.plames.api.messenger.profile.UserProfile;
import com.inwaiders.plames.api.procedure.ProcedureStage;
import com.inwaiders.plames.domain.messenger.profile.impl.UserProfileBase;
import com.inwaiders.plames.domain.procedure.impl.ProcedureImpl;

@Entity
@Table(name = "profile_procedures")
public class ProfileProcedure<S extends ProcedureStage> extends ProcedureImpl<S>{

	@JoinColumn(name = "profile_id")
	@OneToOne(targetEntity = UserProfileBase.class)
	protected UserProfile profile = null;
	
	public void setProfile(UserProfile profile) {
		
		this.profile = profile;
	}
	
	public UserProfile getProfile() {
		
		return this.profile;
	}

	@Override
	public void delete() {
		
		profile.setCurrentProcedure(null);
		profile.save();
		
		super.delete();
	}
}
