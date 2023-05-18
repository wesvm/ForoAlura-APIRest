package com.foro.api.service;

import com.foro.api.exception.ResourceNotFoundException;
import com.foro.api.model.Course;
import com.foro.api.model.Topic;
import com.foro.api.model.User;
import com.foro.api.model.dto.course.DataResponseCourse;
import com.foro.api.model.dto.reply.DataListReplyTopic;
import com.foro.api.model.dto.topic.*;
import com.foro.api.model.dto.user.DataResponseUser;
import com.foro.api.repository.CourseRepository;
import com.foro.api.repository.TopicRepository;
import com.foro.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public Page<DataListTopic> getAll(Pageable pageable){
        Page<Topic> topics = topicRepository.findAll(pageable);
        return topics.map(DataListTopic::new);
    }

    public DataResponseReplyTopic getById(Long id){
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("unable to find requested resource"));
        DataResponseTopic dataTopic = buildDataResponseTopic(topic);

        return new DataResponseReplyTopic(
                dataTopic.id(), dataTopic.title(), dataTopic.content(), dataTopic.creationDate(),
                dataTopic.modificationDate(), dataTopic.status(),
                dataTopic.author(), dataTopic.course(), buildDataListReplyTopics(topic));
    }

    public DataResponseTopic create(DataCreateTopic dataCreateTopic, Authentication authentication){
        User author = userRepository.getReferenceById(dataCreateTopic.authorId());
        Course course = courseRepository.getReferenceById(dataCreateTopic.courseId());

        validateAuthenticatedUser(dataCreateTopic.authorId(), authentication,
                "you are not allowed to create a topic");
        Topic topic = topicRepository.save(new Topic(dataCreateTopic, author, course));

        return buildDataResponseTopic(topic);
    }

    public DataResponseTopic update(DataUpdateTopic dataUpdateTopic, Authentication authentication){
        Topic topic = topicRepository.getReferenceById(dataUpdateTopic.id());
        validateAuthenticatedUser(topic.getAuthor().getId(), authentication,
                "you are not allowed to update this topic");

        if (dataUpdateTopic.courseId().isEmpty()){
            topic.updateTopic(dataUpdateTopic, null);
        } else {
            Course course = courseRepository.getReferenceById(dataUpdateTopic.courseId().get());
            topic.updateTopic(dataUpdateTopic, course);
        }

        return buildDataResponseTopicPut(topic);
    }

    public void delete(Long id, Authentication authentication){
        Topic topic = topicRepository.getReferenceById(id);
        validateAuthenticatedUser(topic.getAuthor().getId(), authentication,
                "you are not allowed to delete this topic");
        topicRepository.delete(topic);
    }

    private void validateAuthenticatedUser(Long authorId, Authentication authentication, String message) {
        User authenticatedUser = (User) authentication.getPrincipal();
        if (!authorId.equals(authenticatedUser.getId())) {
            throw new BadCredentialsException(message);
        }
    }

    private DataResponseTopic buildDataResponseTopic(Topic topic) {
        return new DataResponseTopic(
                topic.getId(), topic.getTitle(), topic.getContent(),
                topic.getCreationDate().toString(), topic.getModificationDate(),
                topic.getTopicStatus().toString(),
                buildDataResponseUser(topic), buildDataResponseCourse(topic)
        );
    }
    private DataResponseTopic buildDataResponseTopicPut(Topic topic) {
        return new DataResponseTopic(
                topic.getId(), topic.getTitle(), topic.getContent(),
                topic.getCreationDate().toString(), topic.getModificationDate(),
                topic.getTopicStatus().toString(),
                null, buildDataResponseCourse(topic)
        );
    }
    private DataResponseUser buildDataResponseUser(Topic topic){
        return new DataResponseUser(
                topic.getAuthor().getId(), topic.getAuthor().getName(), topic.getAuthor().getEmail());
    }

    private DataResponseCourse buildDataResponseCourse(Topic topic){
        return new DataResponseCourse(
                topic.getCourse().getId(), topic.getCourse().getName());
    }

    private List<DataListReplyTopic> buildDataListReplyTopics(Topic topic){
        return topic.getReplies().stream().map(DataListReplyTopic::new).collect(Collectors.toList());
    }

}
