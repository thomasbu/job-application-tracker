package com.tracker.job_application_tracker.controller;

import com.tracker.job_application_tracker.dto.InterviewPrepStatsDTO;
import com.tracker.job_application_tracker.enums.ChallengeStatus;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.service.CodingChallengeService;
import com.tracker.job_application_tracker.service.FlashCardService;
import com.tracker.job_application_tracker.service.SkillService;
import com.tracker.job_application_tracker.service.StudySessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interview-prep/stats")
public class InterviewPrepStatsController {

    private final FlashCardService flashCardService;
    private final CodingChallengeService codingChallengeService;
    private final SkillService skillService;
    private final StudySessionService studySessionService;

    public InterviewPrepStatsController(
            FlashCardService flashCardService,
            CodingChallengeService codingChallengeService,
            SkillService skillService,
            StudySessionService studySessionService) {
        this.flashCardService = flashCardService;
        this.codingChallengeService = codingChallengeService;
        this.skillService = skillService;
        this.studySessionService = studySessionService;
    }

    @GetMapping
    public ResponseEntity<InterviewPrepStatsDTO> getStats(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        InterviewPrepStatsDTO stats = new InterviewPrepStatsDTO();
        stats.setTotalFlashCards(flashCardService.countByUser(user));
        stats.setCardsReviewedToday(flashCardService.countReviewedToday(user));
        stats.setChallengesCompleted(codingChallengeService.countByStatus(user, ChallengeStatus.COMPLETED));
        stats.setChallengesTotal(
                codingChallengeService.countByStatus(user, ChallengeStatus.TODO)
                + codingChallengeService.countByStatus(user, ChallengeStatus.IN_PROGRESS)
                + codingChallengeService.countByStatus(user, ChallengeStatus.COMPLETED)
                + codingChallengeService.countByStatus(user, ChallengeStatus.REVIEW)
        );
        stats.setTotalStudyTimeThisWeek(studySessionService.getTotalMinutesThisWeek(user));
        stats.setAverageSkillLevel(skillService.getAverageLevel(user));

        return ResponseEntity.ok(stats);
    }
}
