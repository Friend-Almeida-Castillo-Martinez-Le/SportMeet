package com.codeup.sportmeet.models;

import javax.persistence.*;

@Entity
@Table(name="restrictions")
public class Restriction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    public Restriction() {
    }

    public Restriction(String name) {
        this.name = name;
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
}
