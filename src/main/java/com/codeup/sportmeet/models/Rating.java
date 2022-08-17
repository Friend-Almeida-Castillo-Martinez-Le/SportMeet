package com.codeup.sportmeet.models;

import javax.persistence.*;

@Entity
@Table(name="ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Player rater;

    @OneToOne
    private Player ratee;

    @OneToOne
    private Event event;

    @Column
    private long rating;

    @Column
    private boolean rated;

    public Rating() {
    }

    public Rating(Player rater, Player ratee, Event event, long rating) {
        this.rater = rater;
        this.ratee = ratee;
        this.event = event;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Player getRater() {
        return rater;
    }

    public void setRater(Player rater) {
        this.rater = rater;
    }

    public Player getRatee() {
        return ratee;
    }

    public void setRatee(Player ratee) {
        this.ratee = ratee;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }
}
