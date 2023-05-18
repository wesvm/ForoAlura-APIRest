package com.foro.api.service;

import com.foro.api.exception.ResourceNotFoundException;
import com.foro.api.exception.TopicClosedException;
import com.foro.api.model.Reply;
import com.foro.api.model.Topic;
import com.foro.api.model.User;
import com.foro.api.model.dto.reply.DataCreateReply;
import com.foro.api.model.dto.reply.DataResponseReply;
import com.foro.api.model.dto.reply.DataUpdateReply;
import com.foro.api.model.dto.user.DataResponseUser;
import com.foro.api.repository.ReplyRepository;
import com.foro.api.repository.TopicRepository;
import com.foro.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    public DataResponseReply getById(Long id){
        Reply reply = replyRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("unable to find requested resource"));
        return buildDataResponseReply(reply);
    }

    public DataResponseReply create(DataCreateReply dataCreateReply, Authentication authentication){
        User author = userRepository.getReferenceById(dataCreateReply.authorId());
        Topic topic = topicRepository.getReferenceById(dataCreateReply.topicId());

        validateAuthenticatedUser(dataCreateReply.authorId(), authentication,
                "you are not allowed to create a reply");
        validateTopicStatus(topic);
        Reply reply = replyRepository.save(new Reply(dataCreateReply, author, topic));
        topic.setTopicStatusNotSolved();

        return buildDataResponseCUReply(reply);
    }

    public DataResponseReply update(DataUpdateReply dataUpdateReply, Authentication authentication){
        Reply reply = replyRepository.getReferenceById(dataUpdateReply.id());
        validateAuthenticatedUser(reply.getAuthor().getId(), authentication,
                "you are not allowed to update this reply");

        reply.updateReply(dataUpdateReply);

        return buildDataResponseCUReply(reply);
    }

    public void delete(Long id, Authentication authentication){
        Reply reply = replyRepository.getReferenceById(id);

        validateAuthenticatedUser(reply.getAuthor().getId(), authentication,
                "you are not allowed to delete this reply");
        replyRepository.delete(reply);
    }

    private void validateAuthenticatedUser(Long authorId, Authentication authentication, String message) {
        User authenticatedUser = (User) authentication.getPrincipal();
        if (!authorId.equals(authenticatedUser.getId())) {
            throw new BadCredentialsException(message);
        }
    }

    private DataResponseReply buildDataResponseReply(Reply reply){
        return new DataResponseReply(
                reply.getId(), reply.getMessage(), reply.getCreationDate().toString(), reply.getModificationDate(),
                new DataResponseUser(
                        reply.getAuthor().getId(), reply.getAuthor().getName(), reply.getAuthor().getEmail()),
                reply.getSolution(), reply.getTopic().getId());
    }

    private DataResponseReply buildDataResponseCUReply(Reply reply){
        return new DataResponseReply(
                reply.getId(), reply.getMessage(), reply.getCreationDate().toString(), reply.getModificationDate(),
                null, reply.getSolution(), reply.getTopic().getId());
    }

    private void validateTopicStatus(Topic topic){
        if (topic.isClosed()){
            throw new TopicClosedException("topic is closed and no more replies can be added");
        }
    }

}
