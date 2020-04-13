package enterprises.inwaiders.plames.dao.messenger.profile;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import enterprises.inwaiders.plames.domain.messenger.profile.impl.SystemProfile;

public interface SystemProfileRepository extends JpaRepository<SystemProfile, Long>{

	@QueryHints({
		@QueryHint(name = "org.hibernate.cacheable", value = "true")
	})
	@Override
	@Query("SELECT p FROM SystemProfile p WHERE p.id = :id AND p.deleted != true")
	public SystemProfile getOne(@Param(value = "id") Long id);
	
	@QueryHints({
		@QueryHint(name = "org.hibernate.cacheable", value = "true")
	})
	@Query("SELECT p FROM SystemProfile p WHERE p.moduleId = :id AND p.deleted != true")
	public SystemProfile getByModuleId(@Param(value = "id") Long id);
	
	@Override
	@Query("SELECT p FROM SystemProfile p WHERE p.deleted != true")
	public List<SystemProfile> findAll();
	
	@Override
	@Query("SELECT COUNT(*) FROM SystemProfile p WHERE p.deleted != true")
	public long count();
}
