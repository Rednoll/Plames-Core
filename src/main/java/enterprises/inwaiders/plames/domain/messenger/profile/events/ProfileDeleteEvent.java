package enterprises.inwaiders.plames.domain.messenger.profile.events;

import enterprises.inwaiders.plames.api.event.PlamesEvent;
import enterprises.inwaiders.plames.api.messenger.profile.UserProfile;

public class ProfileDeleteEvent implements PlamesEvent {

	private UserProfile profile = null;
	
	public ProfileDeleteEvent() {
		
	}
	
	public ProfileDeleteEvent(UserProfile profile) {
		
		this.profile = profile;
	}
	
	public UserProfile getProfile() {
		
		return this.profile;
	}
	
	@Override
	public void dispose() {
		
		this.profile = null;
	}

	@Override
	public boolean getAutoDispose() {
		
		return true;
	}
}
