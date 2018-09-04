package com.wetube.domain.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "tubes")
public class Tube {

    private Integer id;

    private String title;

    private String description;

    private String youtubeId;

    private LocalDateTime addedOn;

    private User publisher;

    private Category category;

    private Set<Tag> tags;

    private Set<User> usersLiked;

    private Set<Comment> comments;

    public Tube() {
        this.tags = new HashSet<>();
        this.usersLiked = new HashSet<>();
        this.comments = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(columnDefinition = "TEXT", nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(nullable = false, name = "youtube_id")
    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    @Column(name = "added_on")
    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }

    @ManyToOne
    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
    public User getPublisher() {
        return publisher;
    }

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tubes_tags",
            joinColumns = @JoinColumn(name = "tube_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @ManyToMany(mappedBy = "likedTubes", fetch = FetchType.EAGER)
    public Set<User> getUsersLiked() {
        return usersLiked;
    }

    public void setUsersLiked(Set<User> usersLiked) {
        this.usersLiked = usersLiked;
    }

    @OneToMany(mappedBy = "tube")
    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @Transient
    public String getSummary() {
        final int maxSymbols = 50;

        if (this.description.length() > maxSymbols) {
            return this.description.substring(0, maxSymbols) + "...";
        }
        return this.description;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }
}
