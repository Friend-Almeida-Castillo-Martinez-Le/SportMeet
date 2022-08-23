package com.codeup.sportmeet.models;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String startTime;

    @Column(nullable = false)
    private String date;

    @Column
    private long playersAttending;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "event_player",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "player_id")}
    )
    private List<Player> players;

    @ManyToOne
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "event")
    private List<Comment> comments;

    @Column
    private String eventPicUrl;

    public Event() {
    }

    public Event(long id, String title, String description, String location, String startTime, String date, Player player, List<Player> players, Sport sport, long playersAttending, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.date = date;
        this.player = player;
        this.players = players;
        this.sport = sport;
        this.playersAttending = playersAttending;
        this.comments = comments;
    }

    public Event(String title, String description, String location, String startTime, String date, Player player, List<Player> players, Sport sport, long playersAttending, List<Comment> comments) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.date = date;
        this.player = player;
        this.players = players;
        this.sport = sport;
        this.playersAttending = playersAttending;
        this.comments = comments;
    }

    public Event(String title, String description, String date, String startTime, String location, Sport sport, long playersAttending, List<Comment> comments) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.location = location;
        this.sport = sport;
        this.playersAttending = playersAttending;
        this.comments = comments;
    }

    public Event(String title, String description, String location, String startTime, String date, long playersAttending, Player player, List<Player> players, Sport sport, List<Comment> comments, String eventPicUrl) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.date = date;
        this.playersAttending = playersAttending;
        this.player = player;
        this.players = players;
        this.sport = sport;
        this.comments = comments;
        this.eventPicUrl = eventPicUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public long getPlayersAttending() {
        return playersAttending;
    }

    public void setPlayersAttending(long playersAttending) {
        this.playersAttending = playersAttending;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getEventPicUrl() {
        return eventPicUrl;
    }

    public void setEventPicUrl(String eventPicUrl) {
        this.eventPicUrl = eventPicUrl;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", startTime='" + startTime + '\'' +
                ", date=" + date +
                ", playersAttending=" + playersAttending +
                ", player=" + player +
                ", players=" + players +
                ", sport=" + sport +
                ", comments=" + comments +
                '}';
    }
}
