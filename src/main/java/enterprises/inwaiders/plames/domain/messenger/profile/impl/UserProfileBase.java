package enterprises.inwaiders.plames.domain.messenger.profile.impl;

import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PostUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import enterprises.inwaiders.plames.CoreModule;
import enterprises.inwaiders.plames.api.command.CommandException;
import enterprises.inwaiders.plames.api.event.EventEngine;
import enterprises.inwaiders.plames.api.event.EventStage;
import enterprises.inwaiders.plames.api.messenger.keyboard.MessengerKeyboard;
import enterprises.inwaiders.plames.api.messenger.profile.UserProfile;
import enterprises.inwaiders.plames.api.procedure.Procedure;
import enterprises.inwaiders.plames.api.user.User;
import enterprises.inwaiders.plames.dao.messenger.profile.UserProfileRepository;
import enterprises.inwaiders.plames.domain.messenger.command.MessengerCommandExecutor;
import enterprises.inwaiders.plames.domain.messenger.message.impl.MessageAgent;
import enterprises.inwaiders.plames.domain.messenger.profile.events.ProfileDeleteEvent;
import enterprises.inwaiders.plames.domain.messenger.profile.procedures.ProfileBindProcedure;
import enterprises.inwaiders.plames.domain.procedure.impl.ProcedureImpl;
import enterprises.inwaiders.plames.domain.user.impl.UserImpl;
import enterprises.inwaiders.plames.system.utils.MessageUtils;

@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "profiles")
@Entity(name = "Profile")
public abstract class UserProfileBase extends MessageAgent implements UserProfile {

	private static transient UserProfileRepository repository;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@JoinColumn(name = "user_id")
	@ManyToOne(targetEntity = UserImpl.class)
	private User user = null;
	
	@Column(name = "accessible", nullable = false)
	private boolean accessible = false;
	
	@JoinColumn(name = "current_procedure_id")
	@OneToOne(cascade = CascadeType.REMOVE, targetEntity = ProcedureImpl.class)
	private Procedure currentProcedure = null;
	
	@Column(name = "deleted")
	protected volatile boolean deleted = false;
	
	public final void setMessengerType(String i) {}
	
	@Column(name = "messenger_type")
	@Access(AccessType.PROPERTY)
	public abstract String getMessengerType();
	
	@Transient
	private MessengerKeyboard keyboard = null;
	
	@PostLoad
	private void postLoad() {
		
		this.keyboard = MessengerKeyboard.load(this.getId());
	}
	
	@PostUpdate
	private void postSave() {
		
		if(this.keyboard != null) {
			
			this.keyboard.save(getId());
		}
	}
	
	public void fromUser(String text) {
		
		if(user == null) {
			
			if(currentProcedure == null || currentProcedure.isEnd()) {
			
				ProfileBindProcedure procedure = ProfileBindProcedure.create(this);
					procedure.begin();
				
				setCurrentProcedure(procedure);
				save();
				
				return;
			}
		}
		
		if(text.startsWith("/")) {

			try {
			
				MessengerCommandExecutor.execute(this, text);
			}
			catch(CommandException e) {
				
				MessageUtils.send(CoreModule.getSystemProfile(), this, e.getLocalizedMessage(), e.getLocaleArgs());
			}
		}
		else {
			
			Procedure procedure = getCurrentProcedure();
		
			if(procedure != null) {
				
				procedure.runNextStage(text);
				
				if(procedure.isEnd()) {
					
					setCurrentProcedure(null);
					
					if(procedure.getAutoDelete()) {
						
						procedure.delete();
					}
				}
			}
		}
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(accessible, currentProcedure, deleted, user, this.getId());
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserProfileBase other = (UserProfileBase) obj;
		return accessible == other.accessible && Objects.equals(currentProcedure, other.currentProcedure)
				&& deleted == other.deleted && Objects.equals(user, other.user);
	}
	
	public boolean isOnline() {
		
		return true;
	}
	
	public String getName() {
		
		return user != null ? user.getNickname() : "user null";
	}

	public void setKeyboard(MessengerKeyboard keyboard) {
		
		this.keyboard = keyboard;
	}

	public MessengerKeyboard getKeyboard() {
		
		return this.keyboard;
	}
	
	public void setCurrentProcedure(Procedure procedure) {
		
		this.currentProcedure = procedure;
	}
	
	public Procedure getCurrentProcedure() {
		
		return this.currentProcedure;
	}
	
	public void setAccessible(boolean access) {
		
		this.accessible = access;
	}
	
	public boolean isAccessible() {
		
		return this.accessible;
	}
	
	public void setUser(User user) {
		
		this.user = user;
	}
	
	public User getUser() {
		
		return this.user;
	}
	
	public void save() {
		
		if(!deleted) {
			
			repository.save(this);
		}
	}
	
	public void delete() {
	
		ProfileDeleteEvent event = new ProfileDeleteEvent(this);
		
		EventEngine.getCommonEngine().run(event, EventStage.PRE);
		
		if(user != null) {
			
			user.getProfilesContainer().removeProfile(this);
			user.save();
		}
		
		if(currentProcedure != null) {
			
			currentProcedure.abort();
			currentProcedure.delete();
			currentProcedure = null;
		}
		
		this.accessible = false;
		
		this.deleted = true;
		repository.saveAndFlush(this);
	
		event = new ProfileDeleteEvent(this);
		
		EventEngine.getCommonEngine().run(event, EventStage.POST);
		
	}
	
	@Override
	public Long getId() {
	
		return this.id;
	}
	
	public static void setRepository(UserProfileRepository rep) {
		
		repository = rep;
	}
}
