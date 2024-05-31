package zerobase.ticketing.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import zerobase.ticketing.domain.CustomUserDetails;
import zerobase.ticketing.domain.Review;
import zerobase.ticketing.repository.ReviewJDBCRepository;
import zerobase.ticketing.repository.ReviewRepository;
import zerobase.ticketing.repository.ShowOpenRepository;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.now;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewJDBCRepository reviewJDBCRepository;
    private final ShowOpenRepository showOpenRepository;

    public ReviewService(ReviewRepository reviewRepository, ReviewJDBCRepository reviewJDBCRepository, ShowOpenRepository showOpenRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewJDBCRepository = reviewJDBCRepository;
        this.showOpenRepository = showOpenRepository;
    }

    public boolean registerReview(Review review) {

        if (reviewRepository.existsByUserIdAndBookingId(review.getUserId(), review.getBookingId())) {
            throw new RuntimeException("이미 리뷰가 등록된 공연입니다.");
        }

        // 관람한 공연에 대하여 리뷰 작성
        LocalDate showDate = showOpenRepository.findShowDateByShowId(review.getShowId());

        if (showDate.isBefore(now())) {
            return false;
        } else {
            review.setRegisterDate(now());
            reviewRepository.save(review);
            return true;
        }
    }

    public List<Review> readShowReview(int showInfoId) {
        // 해당 공연에 대한 리뷰 리스트 조회

        return reviewRepository.findAllByShowInfoId(showInfoId);
    }

    public List<Review> readMyReviewList(Authentication authentication) {
        // 내가 쓴 리뷰 리스트 조회

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();
        return reviewRepository.findAllByUserId(userId);
    }

    public Review readMyReview(Review review) {
        // 내가 쓴 리뷰 조회

        return reviewRepository.findByShowIdAndUserId(review.getShowId(), review.getUserId());
    }

    public boolean isMyReview(Review review, Authentication authentication) {
        // 나의 리뷰인지 확인

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();

        return userId.equals(review.getUserId());
    }

    public void updateMyReview(Review review, Authentication authentication) {
        // 리뷰 수정

        if (!reviewRepository.existsById(review.getId())) {
            throw new RuntimeException("비정상적인 접근입니다.");
        }
        if (!isMyReview(review, authentication)) {
            throw new RuntimeException("수정할 권한이 없습니다.");
        }

        reviewRepository.save(review);
    }

    public void deleteMyReview(Review review, Authentication authentication) {
        // 리뷰 삭제

        if (!reviewRepository.existsById(review.getId())) {
            throw new RuntimeException("비정상적인 접근입니다.");
        }
        if (!isMyReview(review, authentication)) {
            throw new RuntimeException("삭제할 권한이 없습니다.");
        }

        reviewRepository.deleteById(review.getId());
    }

    public List<Review> readPopularShowReview(String genre, LocalDate startDate, LocalDate endDate) {
        // 인기 많은 공연의 후기 조회

        return reviewJDBCRepository.readPopularShowReview(genre, startDate, endDate);
    }
}
