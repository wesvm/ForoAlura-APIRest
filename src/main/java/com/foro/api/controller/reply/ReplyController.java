package com.foro.api.controller.reply;

import com.foro.api.controller.DataResponseError;
import com.foro.api.models.Reply.*;
import com.foro.api.models.Topic.Topic;
import com.foro.api.models.Topic.TopicRepository;
import com.foro.api.models.Topic.TopicStatus;
import com.foro.api.controller.user.DataResponseUser;
import com.foro.api.models.User.User;
import com.foro.api.models.User.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/reply")
public class ReplyController {

    private final ReplyRepository replyRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    public ReplyController(ReplyRepository replyRepository, TopicRepository topicRepository,
            UserRepository userRepository) {
        this.replyRepository = replyRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponseReply> getReplyById(
            @PathVariable Long id) {
        Reply reply = replyRepository.getReferenceById(id);

        var author = new DataResponseUser(
                reply.getAuthor().getId(), reply.getAuthor().getName(), reply.getAuthor().getEmail());

        var dataReply = new DataResponseReply(
                reply.getId(), reply.getMessage(), reply.getCreationDate().toString(), author,
                reply.getSolution());

        return ResponseEntity.ok(dataReply);
    }

    @PostMapping
    public ResponseEntity<?> createReply(
            @Valid @RequestBody DataCreateReply dataCreateReply,
            Authentication authentication,
            UriComponentsBuilder uriComponentsBuilder) {
        User author = userRepository.findById(dataCreateReply.authorId())
                .orElseThrow(() -> new EntityNotFoundException("user not found"));
        Topic topic = topicRepository.findById(dataCreateReply.topicId())
                .orElseThrow(() -> new EntityNotFoundException("topic not found"));

        if (topic.getTopicStatus() == TopicStatus.CLOSED || topic.getTopicStatus() == TopicStatus.SOLVED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new DataResponseError(
                            "topic is closed and no more replies can be added"));
        }

        User authenticadedUser = (User) authentication.getPrincipal();
        if (!dataCreateReply.authorId().equals(authenticadedUser.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new DataResponseError("you are not allowed to create a reply"));
        }

        Reply reply = replyRepository.save(new Reply(dataCreateReply, author, topic));
        topic.setTopicStatusNotSolved();
        topicRepository.save(topic);

        DataResponseReply dataResponseReply = new DataResponseReply(
                reply.getId(), reply.getMessage(), reply.getCreationDate().toString(),
                new DataResponseUser(
                        reply.getAuthor().getId(), reply.getAuthor().getName(),
                        reply.getAuthor().getEmail()),
                reply.getSolution());

        URI url = uriComponentsBuilder.path("/reply/{id}").buildAndExpand(reply.getId()).toUri();
        return ResponseEntity.created(url).body(dataResponseReply);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<?> updateReply(
            @RequestBody @Valid DataUpdateReply dataUpdateReply,
            Authentication authentication) {

        Reply reply = replyRepository.getReferenceById(dataUpdateReply.id());

        User authenticadedUser = (User) authentication.getPrincipal();
        if (!reply.getAuthor().equals(authenticadedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new DataResponseError("you are not allowed to modify this reply"));
        }

        reply.updateReply(dataUpdateReply);
        return ResponseEntity.ok(
                new DataResponseReply(
                        reply.getId(), reply.getMessage(), reply.getCreationDate().toString(),
                        new DataResponseUser(
                                reply.getAuthor().getId(), reply.getAuthor().getName(),
                                reply.getAuthor().getEmail()),
                        reply.getSolution()));
    }

}
