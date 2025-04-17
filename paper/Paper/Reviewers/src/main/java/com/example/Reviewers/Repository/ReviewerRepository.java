package com.example.Reviewers.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Reviewers.Entity.ReviewerEntity;

public interface ReviewerRepository extends JpaRepository<ReviewerEntity, Integer> {
    List<ReviewerEntity> findAllByOrderByIdAsc();
}