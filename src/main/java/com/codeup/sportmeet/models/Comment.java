package com.codeup.sportmeet.models;

import javax.persistence.*;

@Entity
@Table(name="tables")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    public Comment() {
    }

    public Comment(String description) {
        this.description = description;
    }

    public Comment(String description, Player player) {
        this.description = description;
        this.player = player;
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
}
