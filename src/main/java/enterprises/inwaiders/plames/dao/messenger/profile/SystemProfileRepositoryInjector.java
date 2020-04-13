package enterprises.inwaiders.plames.dao.messenger.profile;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enterprises.inwaiders.plames.domain.messenger.profile.impl.SystemProfile;

@Service
public class SystemProfileRepositoryInjector {

	@Autowired
	private SystemProfileRepository repository;

	@PostConstruct
	public void inject() {
		
		SystemProfile.setRepository(repository);
	}
}
