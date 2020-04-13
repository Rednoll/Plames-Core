package enterprises.inwaiders.plames.domain.messenger.message.impl;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import enterprises.inwaiders.plames.api.messenger.keyboard.MessengerKeyboard;
import enterprises.inwaiders.plames.api.messenger.message.Message;
import enterprises.inwaiders.plames.api.messenger.message.MessageReceiver;
import enterprises.inwaiders.plames.api.messenger.message.MessageSender;
import enterprises.inwaiders.plames.api.messenger.message.mask.MessageMask;
import enterprises.inwaiders.plames.api.messenger.profile.UserProfile;
import enterprises.inwaiders.plames.dao.messenger.message.MessageRepository;

@Cache(region = "messages-cache-region", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "Message")
@Table(name = "messages")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class MessageImpl implements Message {

	@Autowired
	protected static transient MessageRepository repository;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@JoinColumn(name = "sender_id")
	@ManyToOne(targetEntity = MessageAgent.class)
	protected MessageSender sender = null;
	
	@JoinColumn(name = "receiver_id")
	@ManyToOne(targetEntity = MessageAgent.class)
	protected MessageReceiver receiver = null;
	
	@Column(name = "delivered")
	protected boolean delivered = false;
	
	@Column(name = "text", length = 65536)
	protected String text = null;
	
	@Column(name = "creation_date")
	protected long creationDate = -1;
	
	@Column(name = "delivery_date")
	protected long deliveryDate = -1;
	
	@Column(name = "deleted")
	protected volatile boolean deleted = false;
	
	@Transient
	private MessengerKeyboard keyboard = null;
	
	@Transient
	public MessageMask mask = null;
	
	public MessageImpl() {
	
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(creationDate, delivered, deliveryDate, id, receiver, text);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageImpl other = (MessageImpl) obj;
		return creationDate == other.creationDate && delivered == other.delivered && deliveryDate == other.deliveryDate
				&& Objects.equals(id, other.id)
				&& Objects.equals(receiver, other.receiver) && Objects.equals(text, other.text);
	}
	
	public void send() {
		
		receiver.receiveMessage(this);
		
		if(receiver instanceof UserProfile) {
			
			((UserProfile) receiver).setKeyboard(this.keyboard);
		}
		
		this.save();
	}
	
	public void setKeyboard(MessengerKeyboard keyboard) {
		
		this.keyboard = keyboard;
	}
	
	public MessengerKeyboard getKeyboard() {
		
		return this.keyboard;
	}
	
	public void setMask(MessageMask mask) {
		
		this.mask = mask;
	}
	
	public MessageMask getMask() {
		
		return this.mask;
	}

	public void setDeliveryDate(long date) {
		
		this.deliveryDate = date;
	}
	
	public long getDeliveryDate() {
		
		return this.deliveryDate;
	}
	
	public void setCreationDate(long date) {
		
		this.creationDate = date;
	}
	
	public long getCreationDate() {
		
		return this.creationDate;
	}
	
	public void setText(String text) {
		
		this.text = text;
	}
	
	public String getText() {
		
		return this.text;
	}
	
	public String getDisplayText() {
		
		if(mask != null) {
			
			return mask.apply(this);
		}
		
		return this.text;
	}
	
	public void setDelivered(boolean delivered) {
		
		this.delivered = delivered;
	}
	
	public boolean isDelivered() {
		
		return this.delivered;
	}
	
	public void setSender(MessageSender sender) {
		
		this.sender = sender;
	}
	
	public MessageSender getSender() {
		
		return this.sender;
	}
	
	public void setReceiver(MessageReceiver receiver) {
		
		this.receiver = receiver;
	}
	
	public MessageReceiver getReceiver() {
		
		return this.receiver;
	}
	
	public Long getId() {
		
		return this.id;
	}
	
	public void save() {
		
		if(!deleted) {
			
			if(keyboard != null) {
				
				keyboard.save(receiver.getId());
			}
			
			repository.save(this);
		}
	}
	
	public void delete() {
		
		deleted = true;
		repository.save(this);
	}
	
	public static MessageImpl create() {
		
		MessageImpl message = new MessageImpl();
		
		message = repository.saveAndFlush(message);
		
		return message;
	}
	
	public static MessageImpl getById(long id) {
		
		return repository.getOne(id);
	}
	
	public static void setRepository(MessageRepository rep) {
		
		repository = rep;
	}
}
