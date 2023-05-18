package com.foro.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foro.api.model.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
