package com.codeup.sportmeet.repositories;

import com.codeup.sportmeet.models.Restriction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestrictionRepository extends JpaRepository<Restriction, Long> {
}
