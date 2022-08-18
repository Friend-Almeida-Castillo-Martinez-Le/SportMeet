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

    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
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
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private List<Event> events;

    @ManyToMany(mappedBy = "players")
    List<Event> attendingEvents;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "player")
    private List<Comment> comments;

    public Player() {
    }

    public Player(Player copy) {
        id = copy.id; // This line is SUPER important! Many things won't work if it's absent
        email = copy.email;
        username = copy.username;
        password = copy.password;
        firstName = copy.firstName;
        lastName = copy.lastName;
    }

    public Player(String username, String password, String email, String firstName, String lastName, List<Comment> comments) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.comments = comments;
    }
    
    
    public Player(long id, String username, String password, String email, String firstName, String lastName, List<Comment> comments) {
       this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.comments = comments;
    }
    
    public Player(String username, String password, String email, String firstName, String lastName, long age, long date, String rating, long upvote, List<Event> events, List<Event> attendingEvents, List<Comment> comments) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.date = date;
        this.rating = rating;
        this.upvote = upvote;
        this.events = events;
        this.attendingEvents = attendingEvents;
        this.comments = comments;
    }
    
    public Player(long id, String username, String password, String email, String firstName, String lastName, long age, long date, String rating, long upvote, List<Event> events, List<Event> attendingEvents, List<Comment> comments) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.date = date;
        this.rating = rating;
        this.upvote = upvote;
        this.events = events;
        this.attendingEvents = attendingEvents;
        this.comments = comments;
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

    public List<Event> getAttendingEvents() {
        return attendingEvents;
    }

    public void setAttendingEvents(List<Event> attendingEvents) {
        this.attendingEvents = attendingEvents;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
