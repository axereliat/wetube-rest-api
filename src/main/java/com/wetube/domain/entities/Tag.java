package com.wetube.domain.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {

    private Integer id;

    private String name;

    private Set<Tube> tubes;

    public Tag() {
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

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "tags", cascade = CascadeType.ALL)
    public Set<Tube> getTubes() {
        return tubes;
    }

    public void setTubes(Set<Tube> tubes) {
        this.tubes = tubes;
    }
}
