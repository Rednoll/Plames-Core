package enterprises.inwaiders.plames.domain.messenger.message.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import enterprises.inwaiders.plames.api.messenger.message.MessageReceiver;
import enterprises.inwaiders.plames.api.messenger.message.MessageSender;

@Entity(name = "MessageAgent")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class MessageAgent implements MessageReceiver, MessageSender {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Override
	public Long getId() {
		
		return this.id;
	}
}
