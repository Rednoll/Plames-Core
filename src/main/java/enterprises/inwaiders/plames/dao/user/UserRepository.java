package enterprises.inwaiders.plames.dao.user;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import enterprises.inwaiders.plames.domain.user.impl.UserImpl;

@Repository
public interface UserRepository extends JpaRepository<UserImpl, Long>{

	@QueryHints({
		@QueryHint(name = "org.hibernate.cacheable", value = "true")
	})
	@Query("SELECT u FROM User u WHERE u.nickname = :nm AND u.deleted != true")
	public UserImpl getByNickname(@Param("nm") String nickname);
	
	@QueryHints({
		@QueryHint(name = "org.hibernate.cacheable", value = "true")
	})
	@Override
	@Query("SELECT u FROM User u WHERE u.id = :id AND u.deleted != true")
	public UserImpl getOne(@Param(value = "id") Long id);
	
	@Override
	@Query("SELECT u FROM User u WHERE u.deleted != true")
	public List<UserImpl> findAll();
	
	@Override
	@Query("SELECT COUNT(*) FROM User u WHERE u.deleted != true")
	public long count();
}