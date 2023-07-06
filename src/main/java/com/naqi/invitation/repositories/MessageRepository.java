package com.naqi.invitation.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.naqi.invitation.models.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
        Page<Message> findByIdGreaterThan(Integer id, Pageable pageable);

}
