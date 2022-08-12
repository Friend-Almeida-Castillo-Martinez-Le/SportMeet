package com.codeup.sportmeet.repositories;


import com.codeup.sportmeet.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
