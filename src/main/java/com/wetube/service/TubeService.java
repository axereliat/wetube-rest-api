package com.wetube.service;

import com.wetube.domain.entities.Tube;
import com.wetube.domain.models.TubeCreateBindingModel;

import java.security.Principal;
import java.util.List;

public interface TubeService {

    void create(TubeCreateBindingModel bindingModel, Principal principal);

    List<Tube> findAll();

    Tube findById(Integer id);

    boolean edit(TubeCreateBindingModel bindingModel, Integer id, Principal principal);

    boolean delete(Integer id, Principal principal);

    boolean likeOrUnlike(Integer id, Principal principal);
}
