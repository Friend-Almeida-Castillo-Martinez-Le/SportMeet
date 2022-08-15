package com.codeup.sportmeet.repositories;


import com.codeup.sportmeet.models.Event;
import com.codeup.sportmeet.models.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Transactional
    @Modifying
    @Query("update Event event set event.title = :title, event.description = :description, event.location = :location, event.startTime = :startTime, event.endTime = :endTime, event.date = :date, event.sport = :sport where event.id = :id")
    void updateEvent(@Param("id") Long id, @Param("title") String title, @Param("description") String description, @Param("location") String location, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("date") Long date, @Param("sport") Sport sport);
}
