package com.inwaiders.plames.dao.messenger.profile;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inwaiders.plames.domain.messenger.profile.impl.UserProfileBase;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileBase, Long>{

	@QueryHints({
		@QueryHint(name = "org.hibernate.cacheable", value = "true")
	})
	@Override
	@Query("SELECT p FROM Profile p WHERE p.id = :id AND p.deleted != true")
	public UserProfileBase getOne(@Param(value = "id") Long id);
	
	@Override
	@Query("SELECT p FROM Profile p WHERE p.deleted != true")
	public List<UserProfileBase> findAll();
	
	@Override
	@Query("SELECT COUNT(*) FROM Profile p WHERE p.deleted != true")
	public long count();
}
