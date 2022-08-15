package com.codeup.sportmeet.repositories;

import com.codeup.sportmeet.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Query("from Player p where p.username like %:term%")
    List<Player> searchByPlayerLike(@Param("term") String term);

    Player findByUsername(String username);
}
