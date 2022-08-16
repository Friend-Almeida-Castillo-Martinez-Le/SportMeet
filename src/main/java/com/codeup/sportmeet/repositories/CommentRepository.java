package com.codeup.sportmeet.repositories;

import com.codeup.sportmeet.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
