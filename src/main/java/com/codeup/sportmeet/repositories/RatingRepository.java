package com.codeup.sportmeet.repositories;

import com.codeup.sportmeet.models.Player;
import com.codeup.sportmeet.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("from Rating r where r.ratee.id=:ratee and r.rated=true")
    List<Rating> searchRatingForRatee(@Param("ratee") Long ratee);
    @Query("from Rating r where r.rater.id=:rater")
    List<Rating> searchRatingForRater(@Param("rater") Long rater);
}
