package zerobase.ticketing.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.ticketing.domain.review.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Boolean existsByUserIdAndBookingId(String userId, int getBookingId);

    List<Review> findAllByShowInfoId(int showInfoId);

    List<Review> findAllByUserId(String userId);

    Review findByShowIdAndUserId(int showId, String userId);
}
