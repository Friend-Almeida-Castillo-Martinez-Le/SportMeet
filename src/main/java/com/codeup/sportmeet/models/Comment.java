package com.codeup.sportmeet.models;

import javax.persistence.*;

@Entity
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    public Comment() {
    }

    public Comment(String description) {
        this.description = description;
    }

    public Comment(String description, Player player) {
        this.description = description;
        this.player = player;
    }

    public Comment(String description, Player player, Event event) {
        this.description = description;
        this.player = player;
        this.event = event;
    }

    public Comment(long id, String description, Player player, Event event) {
        this.id = id;
        this.description = description;
        this.player = player;
        this.event = event;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
