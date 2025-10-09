package com.aipia.tesk.repository;

import com.aipia.tesk.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByMemberId(Long memberId, Pageable pageable);
    Page<Order> findByMemberIdAndOrderedAtAfter(Long memberId, LocalDateTime startDateTime, Pageable pageable);
}