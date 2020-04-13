package enterprises.inwaiders.plames.dao.messenger.message;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enterprises.inwaiders.plames.domain.messenger.message.impl.MessageImpl;

@Service
public class MessageRepositoryInjector {

	@Autowired
	private MessageRepository repository;
	
	@PostConstruct
	private void inject() {
		
		MessageImpl.setRepository(repository);
	}
}
