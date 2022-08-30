package com.codeup.sportmeet.repositories;

import com.codeup.sportmeet.models.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
}
