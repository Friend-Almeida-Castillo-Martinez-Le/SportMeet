package com.codeup.sportmeet.repositories;

import com.codeup.sportmeet.models.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportRepository extends JpaRepository<Sport, Long> {
}
