package enterprises.inwaiders.plames.domain.messenger.profile.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import enterprises.inwaiders.plames.api.messenger.MessengerException;
import enterprises.inwaiders.plames.api.messenger.message.Message;
import enterprises.inwaiders.plames.dao.messenger.profile.SystemProfileRepository;
import enterprises.inwaiders.plames.domain.messenger.message.impl.MessageAgent;

@Entity(name="SystemProfile")
@Table(name="system_profiles")
public class SystemProfile extends MessageAgent {
	
	private static transient SystemProfileRepository repository;
	
	@Column(name="name")
	private String name = null;
	
	@Column(name="module_id")
	private long moduleId = -1;
	
	private volatile boolean deleted = false;
	
	public SystemProfile() {
		
	}
	
	public SystemProfile(String name, long moduleId) {
	
		this.name = name;
		this.moduleId = moduleId;
	}
	
	@Override
	public boolean receiveMessage(Message message) throws MessengerException {
		
		return true;
	}
	
	public long getModuleId() {
		
		return this.moduleId;
	}

	@Override
	public String getName() {
		
		return name;
	}
	
	public void delete() {
		
		this.deleted = true;
		repository.save(this);
	}
	
	public void save() {
		
		if(!deleted) {
		
			repository.save(this);
		}
	}
	
	public static SystemProfile create(String name, long id) {
		
		SystemProfile profile = new SystemProfile(name, id);
		
		profile = repository.save(profile);
		
		return profile;
	}
	
	public static SystemProfile getByModuleId(long id) {
		
		return repository.getByModuleId(id);
	}
	
	public static SystemProfile getById(long id) {
		
		return repository.getOne(id);
	}
	
	public static void setRepository(SystemProfileRepository rep) {
		
		repository = rep;
	}
	
	public static SystemProfileRepository getRepository() {
		
		return repository;
	}
}
