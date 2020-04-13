package enterprises.inwaiders.plames.domain.module.impl;

import enterprises.inwaiders.plames.api.module.Module;
import enterprises.inwaiders.plames.api.module.ModuleStatus;
import enterprises.inwaiders.plames.domain.messenger.profile.impl.SystemProfile;

public abstract class ModuleBase implements Module {

	private ModuleStatus status = ModuleStatus.ACTIVE;
	private SystemProfile profile = null;
	
	public void preInit() {}
	public void postInit() {}
	
	public SystemProfile createProfile() {
		
		return SystemProfile.create(getName(), getId());
	}
	
	public void setProfile(SystemProfile profile) {
		
		this.profile = profile;
	}
	
	public SystemProfile getProfile() {
		
		return this.profile;
	}
	
	public boolean inActiveSide() {
		
		return status == ModuleStatus.ACTIVE || status == ModuleStatus.AWAITING_ON;
	}
	
	public void setStatus(ModuleStatus status) {
		
		this.status = status;
	}
	
	public ModuleStatus getStatus() {
		
		return this.status;
	}
}
