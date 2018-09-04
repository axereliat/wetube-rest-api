package com.wetube.service;

import com.wetube.domain.entities.Tag;
import com.wetube.domain.entities.Tube;
import com.wetube.domain.entities.User;
import com.wetube.domain.models.TubeCreateBindingModel;
import com.wetube.repository.CategoryRepository;
import com.wetube.repository.TagRepository;
import com.wetube.repository.TubeRepository;
import com.wetube.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TubeServiceImpl implements TubeService {

    private final TubeRepository tubeRepository;

    private final CategoryRepository categoryRepository;

    private final TagRepository tagRepository;

    private final UserRepository userRepository;

    @Autowired
    public TubeServiceImpl(TubeRepository tubeRepository, CategoryRepository categoryRepository, TagRepository tagRepository, UserRepository userRepository) {
        this.tubeRepository = tubeRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    private String extractYoutubeId(String link) {
        String[] tokens = link.split("v=|&");

        return tokens[1];
    }

    @Override
    public void create(TubeCreateBindingModel bindingModel, Principal principal) {
        Tube tube = new Tube();

        tube.setAddedOn(LocalDateTime.now());
        tube.setTitle(bindingModel.getTitle());
        tube.setDescription(bindingModel.getDescription());
        tube.setCategory(this.categoryRepository.findById(bindingModel.getCategoryId()).get());
        tube.setPublisher(this.userRepository.findByUsername(principal.getName()));
        tube.setYoutubeId(this.extractYoutubeId(bindingModel.getLink()));

        for (String tagName : bindingModel.getTagStr().split(",\\s*")) {
            if (this.tagRepository.existsByName(tagName)) {
                tube.addTag(this.tagRepository.findByName(tagName));
                continue;
            }
            Tag tag = new Tag();
            tag.setName(tagName);
            this.tagRepository.save(tag);

            tube.addTag(tag);
        }

        this.tubeRepository.saveAndFlush(tube);
    }

    @Override
    public boolean edit(TubeCreateBindingModel bindingModel, Integer id, Principal principal) {
        Optional<Tube> byId = this.tubeRepository.findById(id);
        if (!byId.isPresent()) {
            return false;
        }
        Tube tube = byId.get();
        User user = this.userRepository.findByUsername(principal.getName());
        if (!user.isPublisher(tube)) {
            return false;
        }

        tube.setTitle(bindingModel.getTitle());
        tube.setDescription(bindingModel.getDescription());
        tube.setCategory(this.categoryRepository.findById(bindingModel.getCategoryId()).get());
        tube.setYoutubeId(this.extractYoutubeId(bindingModel.getLink()));

        for (String tagName : bindingModel.getTagStr().split(",\\s*")) {
            if (this.tagRepository.existsByName(tagName)) {
                tube.addTag(this.tagRepository.findByName(tagName));
                continue;
            }
            Tag tag = new Tag();
            tag.setName(tagName);
            this.tagRepository.save(tag);

            tube.addTag(tag);
        }

        this.tubeRepository.saveAndFlush(tube);

        return true;
    }

    @Override
    public boolean delete(Integer id, Principal principal) {
        Optional<Tube> byId = this.tubeRepository.findById(id);
        if (!byId.isPresent()) {
            return false;
        }
        Tube tube = byId.get();
        User user = this.userRepository.findByUsername(principal.getName());
        if (!user.isPublisher(tube)) {
            return false;
        }

        this.tubeRepository.deleteById(tube.getId());

        return true;
    }

    @Override
    public boolean likeOrUnlike(Integer id, Principal principal) {
        User user = this.userRepository.findByUsername(principal.getName());
        Tube tube = this.findById(id);
        if (!user.hasLikedTube(tube)){
            user.likeTube(tube);
            this.userRepository.save(user);
            return true;
        } else {
            user.unlikeTube(tube);
            this.userRepository.save(user);
            return false;
        }
    }

    @Override
    public List<Tube> findAll() {
        return this.tubeRepository.findAll();
    }

    @Override
    public Tube findById(Integer id) {
        return this.tubeRepository.findById(id).get();
    }
}