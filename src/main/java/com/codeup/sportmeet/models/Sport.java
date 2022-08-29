package com.codeup.sportmeet.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "sports")
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String image;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sport")
    private List<Event> events;

    public Sport() {
    }
    public Sport(String name, String image, List<Event> events) {
        this.name = name;
        this.image = image;
        this.events = events;
    }

    public Sport(String name) {
        this.name = name;
    }

    public Sport(long id, String name, List<Event> events) {
        this.id = id;
        this.name = name;
        this.events = events;
    }

    public Sport(String name, List<Event> events) {
        this.name = name;
        this.events = events;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
