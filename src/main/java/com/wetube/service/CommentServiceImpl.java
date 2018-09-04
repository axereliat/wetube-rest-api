package com.wetube.service;

import com.wetube.domain.entities.Comment;
import com.wetube.domain.entities.Tube;
import com.wetube.domain.models.CommentCreateBindingModel;
import com.wetube.repository.CommentRepository;
import com.wetube.repository.TubeRepository;
import com.wetube.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final TubeRepository tubeRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, TubeRepository tubeRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.tubeRepository = tubeRepository;
    }

    @Override
    public boolean post(CommentCreateBindingModel bindingModel, Integer tubeId, Principal principal) {
        Comment comment = new Comment();
        comment.setAuthor(this.userRepository.findByUsername(principal.getName()));
        comment.setAddedOn(LocalDateTime.now());
        Optional<Tube> tubeOptional = this.tubeRepository.findById(tubeId);
        if (!tubeOptional.isPresent()) {
            return false;
        }
        comment.setTube(tubeOptional.get());
        comment.setContent(bindingModel.getContent());

        this.commentRepository.saveAndFlush(comment);

        return true;
    }

    @Override
    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }

    @Override
    public Comment findById(Integer id) {
        return this.commentRepository.findById(id).get();
    }
}