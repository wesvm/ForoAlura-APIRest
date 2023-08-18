package com.foro.api.controller;

import com.foro.api.model.dto.reply.DataCreateReply;
import com.foro.api.model.dto.reply.DataResponseReply;
import com.foro.api.model.dto.reply.DataUpdateReply;

import com.foro.api.service.ReplyService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/replies")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;

    @GetMapping("/{id}")
    public ResponseEntity<DataResponseReply> getReplyById(
            @PathVariable Long id) {
        return ResponseEntity.ok(replyService.getById(id));
    }

    @PostMapping
    public ResponseEntity<DataResponseReply> createReply(
            @Valid @RequestBody DataCreateReply dataCreateReply,
            Authentication authentication,
            UriComponentsBuilder uriComponentsBuilder) {

        var reply = replyService.create(dataCreateReply, authentication);

        URI url = uriComponentsBuilder.path("/reply/{id}").buildAndExpand(reply.id()).toUri();
        return ResponseEntity.created(url).body(reply);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DataResponseReply> updateReply(
            @RequestBody @Valid DataUpdateReply dataUpdateReply,
            Authentication authentication) {
        return ResponseEntity.ok(replyService.update(dataUpdateReply, authentication));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteReply(
            @PathVariable Long id,
            Authentication authentication){
        replyService.delete(id, authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
