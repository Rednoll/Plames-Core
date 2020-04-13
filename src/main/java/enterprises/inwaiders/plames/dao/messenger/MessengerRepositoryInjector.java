package enterprises.inwaiders.plames.dao.messenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enterprises.inwaiders.plames.domain.messenger.impl.MessengerImpl;

@Service
public class MessengerRepositoryInjector {

	@Autowired
	private MessengerRepository repository;
	
	@PostConstruct
	public void inject() {
		
		MessengerImpl.setRepository(repository);
	}
}
