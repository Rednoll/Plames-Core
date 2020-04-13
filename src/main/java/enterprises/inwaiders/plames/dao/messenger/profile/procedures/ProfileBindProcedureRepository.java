package enterprises.inwaiders.plames.dao.messenger.profile.procedures;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import enterprises.inwaiders.plames.dao.procedure.ProcedureRepository;
import enterprises.inwaiders.plames.domain.messenger.profile.procedures.ProfileBindProcedure;

public interface ProfileBindProcedureRepository extends ProcedureRepository<ProfileBindProcedure> {

	@QueryHints({
		@QueryHint(name = "org.hibernate.cacheable", value = "true")
	})
	@Query("SELECT proc FROM ProfileBindProcedure proc WHERE proc.code = :code AND proc.deleted != true")
	public ProfileBindProcedure getByCode(@Param(value = "code") String code);
}
