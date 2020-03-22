package com.inwaiders.plames.domain.messenger.message.mask.impl;

import com.inwaiders.plames.api.messenger.message.mask.impl.MessageMaskImpl;

public class SystemMessageMask extends MessageMaskImpl{

	private static SystemMessageMask instance = new SystemMessageMask();
	
	private SystemMessageMask() {
		
	}
	
	public static SystemMessageMask getInstance() {
		
		return instance;
	}
}
