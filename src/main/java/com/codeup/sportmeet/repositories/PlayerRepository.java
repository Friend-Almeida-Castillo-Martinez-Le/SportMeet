package com.codeup.sportmeet.repositories;

import com.codeup.sportmeet.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
