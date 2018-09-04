package com.wetube.domain.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "cateogries")
@Entity
public class Category {

    private Integer id;

    private String name;

    private Set<Tube> tubes;

    public Category() {
        this.tubes = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "category")
    public Set<Tube> getVideos() {
        return tubes;
    }

    public void setVideos(Set<Tube> tubes) {
        this.tubes = tubes;
    }
}
