package com.inwaiders.plames.dao.messenger.profile.procedures;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.inwaiders.plames.dao.procedure.ProcedureRepository;
import com.inwaiders.plames.domain.messenger.profile.procedures.ProfileBindProcedure;

public interface ProfileBindProcedureRepository extends ProcedureRepository<ProfileBindProcedure> {

	@QueryHints({
		@QueryHint(name = "org.hibernate.cacheable", value = "true")
	})
	@Query("SELECT proc FROM ProfileBindProcedure proc WHERE proc.code = :code AND proc.deleted != true")
	public ProfileBindProcedure getByCode(@Param(value = "code") String code);
}
