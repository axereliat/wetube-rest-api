package com.wetube.web.controllers;

import com.wetube.domain.entities.Category;
import com.wetube.domain.entities.User;
import com.wetube.domain.models.CategoryCreateBindingModel;
import com.wetube.domain.models.CategoryViewModel;
import com.wetube.repository.CategoryRepository;
import com.wetube.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    private final UserService userService;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    @GetMapping(value = "/list", produces = "application/json")
    public List<CategoryViewModel> list() {
        List<Category> categories = this.categoryRepository.findAll();

        List<CategoryViewModel> categoryViewModels = new ArrayList<>();

        for (Category category : categories) {
            CategoryViewModel categoryViewModel = new CategoryViewModel();
            categoryViewModel.setId(category.getId());
            categoryViewModel.setName(category.getName());

            categoryViewModels.add(categoryViewModel);
        }

        return categoryViewModels;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/create", produces = "application/json")
    public Map<String, String> create(@RequestBody CategoryCreateBindingModel bindingModel, Principal principal) {

        Map<String, String> map = new HashMap<>();

        map.put("message", "success");

        User user = this.userService.findByUsername(principal.getName());

        if (!user.isAdmin()) {
            map.put("message", "not admin");
            return map;
        }

        Category category = new Category();

        category.setName(bindingModel.getName());

        this.categoryRepository.saveAndFlush(category);

        return map;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/delete/{id}", produces = "application/json")
    public Map<String, String> delete(Principal principal, @PathVariable Integer id) {
        Map<String, String> map = new HashMap<>();

        map.put("message", "success");

        User user = this.userService.findByUsername(principal.getName());

        if (!user.isAdmin()) {
            map.put("message", "not admin");
            return map;
        }

        if (!this.categoryRepository.existsById(id)) {
            map.put("message", "non-existing id");
            return map;
        }

        this.categoryRepository.deleteById(id);

        return map;
    }
}