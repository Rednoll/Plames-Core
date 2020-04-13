package enterprises.inwaiders.plames.dao.procedure;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import enterprises.inwaiders.plames.domain.procedure.impl.ProcedureImpl;

public interface ProcedureRepository<T extends ProcedureImpl> extends JpaRepository<T, Long>{

	@QueryHints({
		@QueryHint(name = "org.hibernate.cacheable", value = "true")
	})
	@Override
	@Query("SELECT p FROM ProcedureImpl p WHERE p.id = :id AND p.deleted != true")
	public T getOne(@Param(value = "id") Long id);
	
	@Override
	@Query("SELECT p FROM ProcedureImpl p WHERE p.deleted != true")
	public List<T> findAll();
	
	@Override
	@Query("SELECT COUNT(*) FROM ProcedureImpl p WHERE p.deleted != true")
	public long count();
}
