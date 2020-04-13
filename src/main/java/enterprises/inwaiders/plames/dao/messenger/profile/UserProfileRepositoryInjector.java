package enterprises.inwaiders.plames.dao.messenger.profile;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enterprises.inwaiders.plames.domain.messenger.profile.impl.UserProfileBase;

@Service
public class UserProfileRepositoryInjector {

	@Autowired
	private UserProfileRepository repository;

	@PostConstruct
	public void inject() {
		
		UserProfileBase.setRepository(repository);
	}
}
