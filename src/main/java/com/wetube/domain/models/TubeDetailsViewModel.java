package com.wetube.domain.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class TubeDetailsViewModel {

    private Integer id;

    private String title;

    private String description;

    private String youtubeId;

    private String publisher;

    private String category;

    private LocalDateTime addedOn;

    private Set<TagViewModel> tags;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public TubeDetailsViewModel() {
        this.tags = new HashSet<>();
    }

    public Set<TagViewModel> getTags() {
        return tags;
    }

    public void setTags(Set<TagViewModel> tags) {
        this.tags = tags;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }
}
