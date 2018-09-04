package com.wetube.web.controllers;

import com.wetube.domain.entities.Comment;
import com.wetube.domain.entities.Tag;
import com.wetube.domain.entities.Tube;
import com.wetube.domain.entities.User;
import com.wetube.domain.models.*;
import com.wetube.repository.CategoryRepository;
import com.wetube.service.CommentService;
import com.wetube.service.TubeService;
import com.wetube.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tubes")
public class TubeController {

    private final TubeService tubeService;

    private final CategoryRepository categoryRepository;

    private final UserService userService;

    private final CommentService commentService;

    @Autowired
    public TubeController(TubeService tubeService, CategoryRepository categoryRepository, UserService userService, CommentService commentService) {
        this.tubeService = tubeService;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
        this.commentService = commentService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/create", produces = "application/json")
    public Map<String, String> create(@RequestBody TubeCreateBindingModel bindingModel, Principal principal) {
        this.tubeService.create(bindingModel, principal);

        return new HashMap<>();
    }

    @GetMapping(value = "/mostRecent", produces = "application/json")
    public List<TubeViewModel> mostRecent(Integer categoryId, String search) {
        List<Tube> mostRecent;
        if (search == null) search = "";
        if (categoryId == null || categoryId.equals(0)) {
            String finalSearch = search;
            mostRecent = this.tubeService.findAll().stream()
                    .sorted((x1, x2) -> x2.getAddedOn().compareTo(x1.getAddedOn()))
                    .filter(x -> x.getTitle().toLowerCase().contains(finalSearch.toLowerCase()) || (x.getTags().stream().anyMatch(y -> y.getName().toLowerCase().contains(finalSearch.toLowerCase()))))
                    .collect(Collectors.toList());
        } else {
            String finalSearch1 = search;
            mostRecent = this.tubeService.findAll().stream()
                    .filter(x -> x.getCategory().getId().equals(categoryId))
                    .filter(x -> x.getTitle().toLowerCase().contains(finalSearch1.toLowerCase()) || (x.getTags().stream().anyMatch(y -> y.getName().toLowerCase().contains(finalSearch1.toLowerCase()))))
                    .sorted((x1, x2) -> x2.getAddedOn().compareTo(x1.getAddedOn()))
                    .collect(Collectors.toList());
        }

        List<TubeViewModel> tubeViewModels = new ArrayList<>();

        for (Tube tube : mostRecent) {
            TubeViewModel tubeViewModel = new TubeViewModel();
            tubeViewModel.setId(tube.getId());
            tubeViewModel.setDescription(tube.getSummary());
            tubeViewModel.setYoutubeId(tube.getYoutubeId());
            tubeViewModel.setTitle(tube.getTitle());
            tubeViewModel.setCategory(tube.getCategory().getName());
            tubeViewModel.setPublisher(tube.getPublisher().getUsername());

            tubeViewModels.add(tubeViewModel);
        }

        return tubeViewModels;
    }

    @GetMapping(value = "/details/{id}", produces = "application/json")
    public TubeDetailsViewModel details(@PathVariable Integer id) {
        Tube theTube = this.tubeService.findById(id);

        TubeDetailsViewModel tubeDetailsViewModel = new TubeDetailsViewModel();

        tubeDetailsViewModel.setId(theTube.getId());
        tubeDetailsViewModel.setDescription(theTube.getDescription());
        tubeDetailsViewModel.setYoutubeId(theTube.getYoutubeId());
        tubeDetailsViewModel.setTitle(theTube.getTitle());
        tubeDetailsViewModel.setCategory(theTube.getCategory().getName());
        tubeDetailsViewModel.setPublisher(theTube.getPublisher().getUsername());
        tubeDetailsViewModel.setAddedOn(theTube.getAddedOn());

        for (Tag tag : theTube.getTags()) {
            TagViewModel tagViewModel = new TagViewModel();
            tagViewModel.setName(tag.getName());
            tubeDetailsViewModel.getTags().add(tagViewModel);
        }

        return tubeDetailsViewModel;
    }

    @GetMapping(value = "/byTag", produces = "application/json")
    public List<TubeViewModel> byTagName(String tagName) {
        List<Tube> tubes = this.tubeService.findAll().stream()
                    .filter(x -> x.getTags().stream().anyMatch(t -> t.getName().equals(tagName)))
                    .sorted((x1, x2) -> x2.getAddedOn().compareTo(x1.getAddedOn()))
                    .collect(Collectors.toList());

        List<TubeViewModel> tubeViewModels = new ArrayList<>();

        for (Tube tube : tubes) {
            TubeViewModel tubeViewModel = new TubeViewModel();
            tubeViewModel.setId(tube.getId());
            tubeViewModel.setDescription(tube.getSummary());
            tubeViewModel.setYoutubeId(tube.getYoutubeId());
            tubeViewModel.setTitle(tube.getTitle());
            tubeViewModel.setCategory(tube.getCategory().getName());
            tubeViewModel.setPublisher(tube.getPublisher().getUsername());

            tubeViewModels.add(tubeViewModel);
        }

        return tubeViewModels;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/edit/{id}", produces = "application/json")
    public Map<String, String> edit(@RequestBody TubeCreateBindingModel bindingModel, Principal principal, @PathVariable Integer id) {
        Map<String, String> map = new HashMap<>();

        map.put("message", "Success");

        if (!this.tubeService.edit(bindingModel, id, principal)) {
            map.put("message", "Error");
        }

        return map;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/delete/{id}", produces = "application/json")
    public Map<String, String> delete(Principal principal, @PathVariable Integer id) {
        Map<String, String> map = new HashMap<>();

        map.put("message", "Success");

        if (!this.tubeService.delete(id, principal)) {
            map.put("message", "Error");
        }

        return map;
    }

    @GetMapping(value = "/viewModel/{id}", produces = "application/json")
    public TubeCreateBindingModel getViewModel(@PathVariable Integer id) {
        Tube tube = this.tubeService.findById(id);

        TubeCreateBindingModel bindingModel = new TubeCreateBindingModel();
        bindingModel.setTitle(tube.getTitle());
        bindingModel.setDescription(tube.getDescription());
        bindingModel.setCategoryId(tube.getCategory().getId());
        bindingModel.setTagStr(String.join(", ", tube.getTags().stream().map(Tag::getName).collect(Collectors.toList())));
        bindingModel.setLink("https://www.youtube.com/watch?v=" + tube.getYoutubeId());

        return bindingModel;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/likeOrUnlike/{id}", produces = "application/json")
    public Map<String, String> likeOrUnlike(Principal principal, @PathVariable Integer id) {
        Map<String, String> map = new HashMap<>();

        map.put("message", "unliked");

        if (this.tubeService.likeOrUnlike(id, principal)) {
            map.put("message", "liked");
        }

        return map;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/hasLikedTube/{id}", produces = "application/json")
    public Map<String, String> hasLikedTube(Principal principal, @PathVariable Integer id) {
        Map<String, String> map = new HashMap<>();

        map.put("hasLiked", "true");

        if (!this.userService.findByUsername(principal.getName()).hasLikedTube(this.tubeService.findById(id))) {
            map.put("hasLiked", "false");
        }

        return map;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/postComment/{id}", produces = "application/json")
    public Map<String, String> postComment(@RequestBody CommentCreateBindingModel bindingModel, Principal principal, @PathVariable Integer id) {
        Map<String, String> map = new HashMap<>();

        map.put("message", "success");

        if (!this.commentService.post(bindingModel, id, principal)) {
            map.put("message", "non-existing tube id");
        }

        return map;
    }

    @GetMapping(value = "/listComments/{id}", produces = "application/json")
    public List<CommentListViewModel> listComments(@PathVariable Integer id) {
        Tube tube = this.tubeService.findById(id);

        List<CommentListViewModel> commentListViewModels = new ArrayList<>();
        for (Comment comment : tube.getComments().stream().sorted((x1, x2) -> x2.getAddedOn().compareTo(x1.getAddedOn())).collect(Collectors.toList())) {
            CommentListViewModel commentListViewModel = new CommentListViewModel();
            commentListViewModel.setAddedOn(comment.getAddedOn().toString());
            UserViewModel userViewModel = new UserViewModel();
            userViewModel.setId(comment.getAuthor().getId());
            userViewModel.setUsername(comment.getAuthor().getUsername());
            userViewModel.setAvatar(comment.getAuthor().getAvatar());
            commentListViewModel.setAuthor(userViewModel);
            commentListViewModel.setId(comment.getId());
            commentListViewModel.setContent(comment.getContent());

            commentListViewModels.add(commentListViewModel);
        }

        return commentListViewModels;
    }

    @GetMapping(value = "/deleteComment/{id}", produces = "application/json")
    public Map<String, String> deleteComment(@PathVariable Integer id, Principal principal) {
        User user = this.userService.findByUsername(principal.getName());
        Comment comment = this.commentService.findById(id);

        Map<String, String> map = new HashMap<>();

        map.put("message", "success");

        if (!user.hasWrittenComment(comment)) {
            map.put("message", "forbidden");
        }

        this.commentService.delete(comment);

        return map;
    }

    @GetMapping(value = "/likesCount/{id}", produces = "application/json")
    public Map<String, String> getLikesCount(@PathVariable Integer id, Principal principal) {
        Tube tube = this.tubeService.findById(id);

        Map<String, String> map = new HashMap<>();

        map.put("likesCount", String.valueOf(tube.getUsersLiked().size()));

        return map;
    }
}