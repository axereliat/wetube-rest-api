package com.wetube.service;

import com.wetube.domain.entities.Comment;
import com.wetube.domain.models.CommentCreateBindingModel;

import java.security.Principal;

public interface CommentService {

    boolean post(CommentCreateBindingModel bindingModel, Integer tubeId, Principal principal);

    Comment findById(Integer id);

    void delete(Comment comment);
}
