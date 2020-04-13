package enterprises.inwaiders.plames.domain.messenger.impl;

import java.util.List;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import enterprises.inwaiders.plames.api.messenger.Messenger;
import enterprises.inwaiders.plames.api.messenger.profile.UserProfile;
import enterprises.inwaiders.plames.dao.messenger.MessengerRepository;

@Cache(region = "messengers-cache-region", usage = CacheConcurrencyStrategy.READ_WRITE)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "messengers")
@Entity
public abstract class MessengerImpl<P extends UserProfile> implements Messenger<P>{

	@Autowired
	private static transient MessengerRepository repository;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Column(name = "active")
	private boolean active = true;

	@Column(name = "type", updatable = false)
	private String type = null;
	
	@Column(name = "name", updatable = false)
	private String name = null;
	
	@Column(name = "deleted")
	private volatile boolean deleted = false;
	
	@Override
	public int hashCode() {
		
		return Objects.hash(active, id, type);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessengerImpl other = (MessengerImpl) obj;
		return active == other.active && Objects.equals(id, other.id) && Objects.equals(type, other.type);
	}
	
	public void init() {}

	/*
	public void applyStyle(Message message) {
		
		Profile source = message.getSourceProfile();
		User sender = message.getSender();
		
		String rawText = message.getText();
		
		rawText = "["+source.getMessengerType()+"] <"+sender.getNickname()+"> "+rawText;
		
		message.setText(rawText);
	}
	*/
	
	@Access(AccessType.PROPERTY)
	private void setName(String name) {
	
		this.name = name;
	}
	
	@Access(AccessType.PROPERTY)
	public abstract String getName();
	
	@Access(AccessType.PROPERTY)
	private final void setType(String type) {}
	
	@Access(AccessType.PROPERTY)
	public abstract String getType();
	
	public void setActive(boolean active) {
		
		this.active = active;
	}
	
	public boolean isActive() {
		
		return active;
	}
	
	public Long getId() {
		
		return this.id;
	}
	
	public void save() {
		
		if(!deleted) {
		
			repository.save(this);
		}
	}
	
	public void delete() {
		
		deleted = true;
		repository.save(this);
	}

	public static MessengerImpl getById(long id) {
		
		return repository.getOne(id);
	}
	
	public static MessengerImpl getByType(String type) {
		
		return repository.getByType(type);
	}
	
	public static List<MessengerImpl> getAll() {
		
		return repository.findAll();
	}
	
	public static void setRepository(MessengerRepository rep) {
		
		repository = rep;
	}
}
