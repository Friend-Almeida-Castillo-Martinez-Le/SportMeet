package com.codeup.sportmeet.models;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

@Entity
@Table(name="players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column
    private long age;

    @Column(length = 8)
    private long date;

    @Column
    private String rating;

    @Column
    private long upvote;

    @Basic
    private java.sql.Time sqlTime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<Event> events;

    @ManyToMany(mappedBy = "players")
    List<Event> games;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
//
//    @ManyToMany(mappedBy = "events")
//    private List events;


    public Player() {
    }

    public Player(String username, String password, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Player(String username, String password, String email, String firstName, String lastName, long age, long date, String rating, long upvote, Time sqlTime, List<Event> events, List<Event> games, Event event) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.date = date;
        this.rating = rating;
        this.upvote = upvote;
        this.sqlTime = sqlTime;
        this.events = events;
        this.games = games;
        this.event = event;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public long getUpvote() {
        return upvote;
    }

    public void setUpvote(long upvote) {
        this.upvote = upvote;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Event> getGames() {
        return games;
    }

    public void setGames(List<Event> games) {
        this.games = games;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

}
