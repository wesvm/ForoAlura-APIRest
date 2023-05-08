package com.foro.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foro.api.models.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
