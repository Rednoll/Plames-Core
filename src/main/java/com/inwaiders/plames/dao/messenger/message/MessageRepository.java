package com.inwaiders.plames.dao.messenger.message;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inwaiders.plames.domain.messenger.message.impl.MessageImpl;

@Repository
public interface MessageRepository extends JpaRepository<MessageImpl, Long>{

	@QueryHints({
		@QueryHint(name = "org.hibernate.cacheable", value = "true")
	})
	@Override
	@Query("SELECT m FROM Message m WHERE m.id = :id AND m.deleted != true")
	public MessageImpl getOne(@Param(value = "id") Long id);
	
	@Override
	@Query("SELECT m FROM Message m WHERE m.deleted != true")
	public List<MessageImpl> findAll();
	
	@Override
	@Query("SELECT COUNT(*) FROM Message m WHERE m.deleted != true")
	public long count();
}
