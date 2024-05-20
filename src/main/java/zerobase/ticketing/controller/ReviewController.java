package zerobase.ticketing.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zerobase.ticketing.domain.Review;
import zerobase.ticketing.service.ReviewService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/register/review")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> registerReview(@RequestBody Review review) {
        // 리뷰 등록

        if(reviewService.registerReview(review)) {
            return ResponseEntity.ok(review);
        } else {
            throw new RuntimeException("불가한 접근입니다.");
        }
    }

    @GetMapping("/read/showReview")
    public List<Review> readShowReview(@RequestParam("showInfoId") int showInfoId) {
        // 해당 공연에 대한 리뷰 리스트 조회

        return reviewService.readShowReview(showInfoId);
    }

    @PostMapping("/read/myReview")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Review> readMyReview(Authentication authentication) {
        // 내가 쓴 리뷰 리스트 조회

        return reviewService.readMyReviewList(authentication);
    }

    @PostMapping("/update/myReview")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void updateMyReview(@RequestBody Review review,
                               @Validated Authentication authentication) {
        // 리뷰 수정

        reviewService.updateMyReview(review, authentication);
    }

    @PostMapping("/delete/myReview")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteMyReview(@RequestBody Review review, Authentication authentication) {
        // 리뷰 삭제

        reviewService.deleteMyReview(review, authentication);
    }

    @PostMapping("read/popularShowReview")
    public List<Review> readPopularShowReview(@RequestBody String genre,
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        // 인기 많은 공연의 후기 조회

        return reviewService.readPopularShowReview(genre, startDate, endDate);
    }
}
