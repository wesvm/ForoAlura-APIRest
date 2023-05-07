package com.foro.api.controller.topic;

import com.foro.api.controller.DataResponseError;
import com.foro.api.models.Course.Course;
import com.foro.api.models.Course.CourseRepository;
import com.foro.api.controller.course.DataResponseCourse;
import com.foro.api.controller.reply.DataListReplyTopic;
import com.foro.api.models.Topic.*;
import com.foro.api.controller.user.DataResponseUser;
import com.foro.api.models.User.User;
import com.foro.api.models.User.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topic")
public class TopicController {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public TopicController(TopicRepository topicRepository, UserRepository userRepository,
                           CourseRepository courseRepository) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public Page<DataListTopic> getAllTopics(
            @PageableDefault(size = 20)
            Pageable pageable){
        Page<Topic> topics = topicRepository.findAll(pageable);
        return topics.map(DataListTopic::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponseReplyTopic> getTopicById(
            @PathVariable Long id
    ){
        Topic topic = topicRepository.getReferenceById(id);

        var author = new DataResponseUser(
                topic.getAuthor().getId(), topic.getAuthor().getName(), topic.getAuthor().getEmail());
        var course = new DataResponseCourse(
                topic.getCourse().getId(), topic.getCourse().getName());

        DataResponseTopic dataTopic = new DataResponseTopic(
                topic.getId(), topic.getTitle(), topic.getContent(),
                topic.getCreationDate().toString(), topic.getTopicStatus().toString(),
                author, course);

        List<DataListReplyTopic> dataListReplyTopics = topic.getReplies().stream()
                .map(DataListReplyTopic::new).collect(Collectors.toList());

        DataResponseReplyTopic dataResponseReplyTopic = new DataResponseReplyTopic(
                dataTopic.id(), dataTopic.title(), dataTopic.content(), dataTopic.creationDate(), dataTopic.status(),
                dataTopic.author(), dataTopic.course(), dataListReplyTopics
        );

        return ResponseEntity.ok(dataResponseReplyTopic);
    }

    @PostMapping
    public ResponseEntity<?> createTopic(
            @Valid @RequestBody
            DataCreateTopic dataCreateTopic,
            Authentication authentication,
            UriComponentsBuilder uriComponentsBuilder
    ){
        User author = userRepository.findById(dataCreateTopic.authorId())
                .orElseThrow(()-> new EntityNotFoundException("user not found"));
        Course course = courseRepository.findById(dataCreateTopic.courseId())
                .orElseThrow(()-> new EntityNotFoundException("course not found"));

        User authenticadedUser = (User) authentication.getPrincipal();
        if (!dataCreateTopic.authorId().equals(authenticadedUser.getId())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new DataResponseError("you are not allowed to create a topic"));
        }

        Topic topic = topicRepository.save(new Topic(dataCreateTopic, author, course));
        DataResponseTopic dataResponseTopic = new DataResponseTopic(
                topic.getId(), topic.getTitle(), topic.getContent(), topic.getCreationDate().toString(),
                topic.getTopicStatus().toString(),
                new DataResponseUser(
                        topic.getAuthor().getId(), topic.getAuthor().getName(), topic.getAuthor().getEmail()),
                new DataResponseCourse(
                        topic.getCourse().getId(), topic.getCourse().getName()
                ));

        URI url = uriComponentsBuilder.path("/topic/{id}").buildAndExpand(topic.getId()).toUri();
        return ResponseEntity.created(url).body(dataResponseTopic);
    }



}