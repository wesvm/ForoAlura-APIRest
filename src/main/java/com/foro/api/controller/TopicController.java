package com.foro.api.controller;

import com.foro.api.model.dto.topic.*;
import com.foro.api.service.TopicService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping
    public Page<DataListTopic> getAllTopics(
            @PageableDefault(size = 20) Pageable pageable) {
        return topicService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponseReplyTopic> getTopicById(
            @PathVariable Long id) {
        return ResponseEntity.ok(topicService.getById(id));
    }

    @PostMapping
    public ResponseEntity<DataResponseTopic> createTopic(
            @Valid @RequestBody DataCreateTopic dataCreateTopic,
            Authentication authentication,
            UriComponentsBuilder uriComponentsBuilder) {

        var topic = topicService.create(dataCreateTopic, authentication);

        URI url = uriComponentsBuilder.path("/topic/{id}").buildAndExpand(topic.id()).toUri();
        return ResponseEntity.created(url).body(topic);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DataResponseTopic> updateTopic(
            @RequestBody @Valid DataUpdateTopic dataUpdateTopic,
            Authentication authentication) {
        return ResponseEntity.ok(topicService.update(dataUpdateTopic, authentication));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteTopic(
            @PathVariable Long id,
            Authentication authentication
    ){
        topicService.delete(id, authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
