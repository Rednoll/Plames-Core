package enterprises.inwaiders.plames.system.utils;

import enterprises.inwaiders.plames.api.locale.PlamesLocale;
import enterprises.inwaiders.plames.api.messenger.keyboard.MessengerKeyboard;
import enterprises.inwaiders.plames.api.messenger.message.Message;
import enterprises.inwaiders.plames.api.messenger.message.MessageReceiver;
import enterprises.inwaiders.plames.api.messenger.message.MessageSender;
import enterprises.inwaiders.plames.api.messenger.profile.UserProfile;
import enterprises.inwaiders.plames.api.user.User;
import enterprises.inwaiders.plames.domain.messenger.message.impl.MessageImpl;
import enterprises.inwaiders.plames.domain.messenger.message.mask.impl.SystemMessageMask;

public class MessageUtils {

	public static void send(MessageSender sender, MessageReceiver target, String text) {

		send(sender, target, text, null, new Object[0]);
	}
	
	public static void send(MessageSender sender, MessageReceiver target, String text, Object... args) {

		send(sender, target, text, null, args);
	}
	
	public static void send(MessageSender sender, MessageReceiver target, String text, MessengerKeyboard keyboard, Object... args) {
		
		if(text.startsWith("$")) {

			if(target instanceof UserProfile) {
				
				UserProfile profile = (UserProfile) target;

				User user = profile.getUser();

				if(user != null) {

					PlamesLocale locale = user.getLocale();

					if(locale != null) {
						
						text = locale.getMessage(text.substring(1), args);
					}
				}
			}
		}

		Message message = MessageImpl.create();
			message.setReceiver(target);
			message.setSender(sender);
			message.setText(text);
			message.setKeyboard(keyboard);
			message.setMask(SystemMessageMask.getInstance());
			message.setCreationDate(System.currentTimeMillis());
			
		message.save();
		message.send();
	}
}
