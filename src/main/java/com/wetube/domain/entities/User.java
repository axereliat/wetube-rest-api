package com.wetube.domain.entities;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    private Integer id;

    private String email;

    private String username;

    private String password;

    private Set<Role> authorities;

    private String avatar;

    private Set<Tube> likedTubes;

    private Set<Comment> comments;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    private boolean isEnabled;

    public User() {
        this.authorities = new HashSet<>();
        this.comments = new HashSet<>();
        this.likedTubes = new HashSet<>();
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToMany(mappedBy = "author")
    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @Override
    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    @ManyToMany
    @JoinTable(name = "likes",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tube_id", referencedColumnName = "id"))
    public Set<Tube> getLikedTubes() {
        return this.likedTubes;
    }

    public void setLikedTubes(Set<Tube> likedTubes) {
        this.likedTubes = likedTubes;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Transient
    public void likeTube(Tube tube) {
        this.likedTubes.add(tube);
    }

    @Transient
    public void unlikeTube(Tube tube) {
        this.likedTubes.remove(tube);
    }

    @Transient
    public boolean hasLikedTube(Tube tube) {
        return this.likedTubes.contains(tube);
    }

    @Transient
    public void addRole(Role role) {
        this.authorities.add(role);
    }

    @Transient
    public boolean isAdmin() {
        return this.authorities.stream().anyMatch(x -> x.getAuthority().equals("ADMIN"));
    }

    @Transient
    public boolean isPublisher(Tube tube) {
        return tube.getPublisher().getId().equals(this.id);
    }

    @Transient
    public boolean hasWrittenComment(Comment comment) {
        return this.comments.contains(comment);
    }
}
