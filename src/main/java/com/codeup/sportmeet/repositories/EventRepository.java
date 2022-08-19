package com.codeup.sportmeet.repositories;


import com.codeup.sportmeet.models.Event;
import com.codeup.sportmeet.models.Player;
import com.codeup.sportmeet.models.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Transactional
    @Modifying
    @Query("update Event event set event.title = :title, event.description = :description, event.location = :location, event.startTime = :startTime, event.date = :date, event.sport = :sport where event.id = :id")
    void updateEvent(@Param("id") Long id, @Param("title") String title, @Param("description") String description, @Param("location") String location, @Param("startTime") String startTime, @Param("date") String date, @Param("sport") Sport sport);

    @Query("from Event event where event.title like %:search%")
    List<Event> searchEventsByTitle(@Param("search") String search);

    @Query("from Event event where event.description like %:search%")
    List<Event> searchEventsByDescription(@Param("search") String search);

    @Query("from Event event where event.sport.name like %:search%")
    List<Event> searchEventsBySportName(@Param("search") String search);
}
