package enterprises.inwaiders.plames.dao.messenger;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import enterprises.inwaiders.plames.domain.messenger.impl.MessengerImpl;

@Repository
public interface MessengerRepository extends JpaRepository<MessengerImpl, Long>{

	@QueryHints({
		@QueryHint(name = "org.hibernate.cacheable", value = "true")
	})
	@Query("SELECT m FROM MessengerImpl m WHERE m.type = :type AND m.deleted != true")
	public MessengerImpl getByType(@Param("type") String type);
	
	@QueryHints({
		@QueryHint(name = "org.hibernate.cacheable", value = "true")
	})
	@Override
	@Query("SELECT m FROM MessengerImpl m WHERE m.id = :id AND m.deleted != true")
	public MessengerImpl getOne(@Param(value = "id") Long id);
	
	@Override
	@Query("SELECT m FROM MessengerImpl m WHERE m.deleted != true")
	public List<MessengerImpl> findAll();
	
	@Override
	@Query("SELECT COUNT(*) FROM MessengerImpl m WHERE m.deleted != true")
	public long count();
}
