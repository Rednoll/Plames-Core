package com.inwaiders.plames.domain.user.events;

import com.inwaiders.plames.api.event.PlamesEvent;
import com.inwaiders.plames.api.user.User;

public class UserCreateEvent implements PlamesEvent {

	private User user = null;
	
	public UserCreateEvent() {
	
	}
	
	public UserCreateEvent(User user) {
		
		this.user = user;
	}
	
	@Override
	public void dispose() {
		
		this.user = null;
	}
	
	public User getUser() {
		
		return this.user;
	}
	
	public boolean getAutoDispose() {
		
		return true;
	}
}
