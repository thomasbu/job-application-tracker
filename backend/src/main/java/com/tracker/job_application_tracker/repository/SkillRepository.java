package com.tracker.job_application_tracker.repository;

import com.tracker.job_application_tracker.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByUserIdOrderByCategoryAsc(Long userId);

    Optional<Skill> findByUserIdAndId(Long userId, Long id);
}
