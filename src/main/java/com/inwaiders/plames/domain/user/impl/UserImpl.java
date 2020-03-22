package com.inwaiders.plames.domain.user.impl;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import com.inwaiders.plames.api.event.EventEngine;
import com.inwaiders.plames.api.event.EventStage;
import com.inwaiders.plames.api.locale.PlamesLocale;
import com.inwaiders.plames.api.messenger.profile.additions.ProfilesContainer;
import com.inwaiders.plames.api.user.User;
import com.inwaiders.plames.dao.EntityLink;
import com.inwaiders.plames.dao.user.UserHlRepository;
import com.inwaiders.plames.dao.user.UserRepository;
import com.inwaiders.plames.domain.locale.PlamesLocaleImpl.HighLevelRepository.PlamesLocaleConverter;
import com.inwaiders.plames.domain.messenger.profile.additions.ProfilesContainerImpl;
import com.inwaiders.plames.domain.user.events.UserCreateEvent;
import com.inwaiders.plames.spring.SpringUtils;

@Cache(region = "users-cache-region", usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity(name = "User")
@Table(name = "users")
public class UserImpl implements User{

	@Autowired
	private static transient UserRepository repository;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Column(name = "nickname")
	protected String nickname = null;
	
	@Embedded
	protected ProfilesContainerImpl profilesContainer = new ProfilesContainerImpl();
	
	@Column(name = "deleted")
	protected volatile boolean deleted = false;
	
	@Column(name = "locale")
	@Convert(converter = PlamesLocaleConverter.class)
	protected PlamesLocale locale = null;
	
	@Override
	public int hashCode() {
		
		return Objects.hash(id, nickname, locale);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserImpl other = (UserImpl) obj;
		return Objects.equals(id, other.id) && Objects.equals(nickname, other.nickname)
//				&& Objects.equals(profilesContainer, other.profilesContainer) && Objects.equals(locale, other.locale);
				&& Objects.equals(locale, other.locale);
	
	}

	public ProfilesContainer getProfilesContainer() {
		
		return this.profilesContainer;
	}
	
	public void setLocale(PlamesLocale locale) {
		
		this.locale = locale;
	}
	
	public PlamesLocale getLocale() {
		
		return this.locale;
	}
	
	public void setNickname(String nickname) {
		
		this.nickname = nickname;
	}
	
	public String getNickname() {
		
		return this.nickname;
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
	
	public static UserImpl create() {
		
		UserImpl newUser = new UserImpl();
		
		newUser = repository.saveAndFlush(newUser);
		
		UserCreateEvent event = new UserCreateEvent(newUser);
		
		EventEngine.getCommonEngine().run(event, EventStage.POST);
		
		return newUser;
	}
	
	public static void flush() {
		
		repository.flush();
	}
	
	public static UserImpl getByNickname(String nickname) {
		
		return repository.getByNickname(nickname);
	}
	
	public static UserImpl getById(long id) {
		
		return repository.getOne(id);
	}
	
	public static List<UserImpl> getAll() {
		
		return repository.findAll();
	}
	
	public static void setRepository(UserRepository rep) {
		
		repository = rep;
	}
	
	public static class HighLevelRepository extends UserHlRepository<UserImpl> {
		
		public HighLevelRepository() {
			
		}
		
		@Override
		public EntityLink getLink(UserImpl user) {
			
			return new EntityLink(SpringUtils.getEntityName(UserImpl.class), user.getId());
		}

		@Override
		public void save(UserImpl user) {
			
			user.save();
		}
		
		@Override
		public UserImpl getById(Long id) {
			
			return UserImpl.getById(id);
		}
	}
}
