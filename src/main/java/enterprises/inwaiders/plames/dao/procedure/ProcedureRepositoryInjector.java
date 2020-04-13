package enterprises.inwaiders.plames.dao.procedure;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enterprises.inwaiders.plames.domain.messenger.profile.procedures.ProfileProcedure;
import enterprises.inwaiders.plames.domain.procedure.impl.ProcedureImpl;

@Service
public class ProcedureRepositoryInjector {

	@Autowired
	private ProcedureRepository<ProfileProcedure> repository;
	
	@PostConstruct
	private void inject() {
		
		ProcedureImpl.setRepository(repository);
	}
}
