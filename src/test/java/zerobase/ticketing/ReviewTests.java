package zerobase.ticketing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.ticketing.domain.Review;
import zerobase.ticketing.domain.ShowOpen;
import zerobase.ticketing.repository.ReviewRepository;
import zerobase.ticketing.repository.ShowOpenRepository;
import zerobase.ticketing.service.ReviewService;
import zerobase.ticketing.service.ShowOpenService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ReviewTests {

	private final ReviewService reviewService;
	private final ReviewRepository reviewRepository;
	private final ShowOpenService showOpenService;
	private final ShowOpenRepository showOpenRepository;

	@Autowired
	public ReviewTests(ReviewService reviewService, ReviewRepository reviewRepository,
					   ShowOpenService showOpenService, ShowOpenRepository showOpenRepository) {
		this.reviewService = reviewService;
		this.reviewRepository = reviewRepository;
		this.showOpenService = showOpenService;
		this.showOpenRepository = showOpenRepository;
	}

	@Test
	@Transactional
	void registerReview() {
	    // given
		ShowOpen newShowOpen = ShowOpen.builder()
				.showInfoId(1)
				.showDate(LocalDate.parse("2024-07-01"))
				.showTime(LocalTime.parse("19:00"))
				.openBooking(LocalDate.parse("2024-06-01"))
				.openBookingTime(LocalTime.parse("20:00"))
				.closeBooking(LocalDate.parse("2024-06-30"))
				.closeBookingTime(LocalTime.parse("18:00"))
				.cast("수료생").build();
		List<ShowOpen> showOpenList = Arrays.asList(
				newShowOpen
		);
		showOpenService.registerShowOpen(showOpenList);
		int showId = showOpenRepository.findShowId(newShowOpen.getShowInfoId(), newShowOpen.getShowDate(), newShowOpen.getShowTime());

		Review review = Review.builder()
				.bookingId(1)
				.userId("ayj")
				.showInfoId(1)
				.showId(showId)
				.showName("제로베이스 수료식")
				.registerDate(LocalDate.parse("2024-05-10"))
				.text("리뷰입니다.")
				.build();

	    // when
		reviewService.registerReview(review);
		Boolean isExists = reviewRepository.existsByUserIdAndBookingId(review.getUserId(), review.getBookingId());

	    // then
		assertEquals(true, isExists);
	}

	@Test
	@Transactional
	void readShowReview() {
	    // given
		ShowOpen newShowOpen = ShowOpen.builder()
				.showInfoId(1)
				.showDate(LocalDate.parse("2024-07-01"))
				.showTime(LocalTime.parse("19:00"))
				.openBooking(LocalDate.parse("2024-06-01"))
				.openBookingTime(LocalTime.parse("20:00"))
				.closeBooking(LocalDate.parse("2024-06-30"))
				.closeBookingTime(LocalTime.parse("18:00"))
				.cast("수료생").build();
		List<ShowOpen> showOpenList = Arrays.asList(
				newShowOpen
		);
		showOpenService.registerShowOpen(showOpenList);
		int showId = showOpenRepository.findShowId(newShowOpen.getShowInfoId(), newShowOpen.getShowDate(), newShowOpen.getShowTime());

		Review review = Review.builder()
				.bookingId(1)
				.userId("ayj")
				.showInfoId(1)
				.showId(showId)
				.showName("제로베이스 수료식")
				.registerDate(LocalDate.parse("2024-05-10"))
				.text("리뷰입니다.")
				.build();

		reviewService.registerReview(review);

	    // when
		List<Review> list = reviewService.readShowReview(review.getShowInfoId());

	    // then
		assertEquals(1, list.size());
	}

	@Test
	@Transactional
	void updateMyReview() {
	    // given
		ShowOpen newShowOpen = ShowOpen.builder()
				.showInfoId(1)
				.showDate(LocalDate.parse("2024-07-01"))
				.showTime(LocalTime.parse("19:00"))
				.openBooking(LocalDate.parse("2024-06-01"))
				.openBookingTime(LocalTime.parse("20:00"))
				.closeBooking(LocalDate.parse("2024-06-30"))
				.closeBookingTime(LocalTime.parse("18:00"))
				.cast("수료생").build();
		List<ShowOpen> showOpenList = Arrays.asList(
				newShowOpen
		);
		showOpenService.registerShowOpen(showOpenList);
		int showId = showOpenRepository.findShowId(newShowOpen.getShowInfoId(), newShowOpen.getShowDate(), newShowOpen.getShowTime());

		Review review = Review.builder()
				.bookingId(1)
				.userId("ayj")
				.showInfoId(1)
				.showId(showId)
				.showName("제로베이스 수료식")
				.registerDate(LocalDate.parse("2024-05-10"))
				.text("리뷰입니다.")
				.build();

		reviewService.registerReview(review);

	    // when
		String changingText = "리뷰입니다. 화이팅!";
		review.setText(changingText);
		reviewRepository.save(review);

		Review resultedReview = reviewService.readMyReview(review);

	    // then
		assertEquals(changingText, resultedReview.getText());
	}

	@Test
	@Transactional
	void deleteMyReview() {
	    // given
		ShowOpen newShowOpen = ShowOpen.builder()
				.showInfoId(1)
				.showDate(LocalDate.parse("2024-07-01"))
				.showTime(LocalTime.parse("19:00"))
				.openBooking(LocalDate.parse("2024-06-01"))
				.openBookingTime(LocalTime.parse("20:00"))
				.closeBooking(LocalDate.parse("2024-06-30"))
				.closeBookingTime(LocalTime.parse("18:00"))
				.cast("수료생").build();
		List<ShowOpen> showOpenList = Arrays.asList(
				newShowOpen
		);
		showOpenService.registerShowOpen(showOpenList);
		int showId = showOpenRepository.findShowId(newShowOpen.getShowInfoId(), newShowOpen.getShowDate(), newShowOpen.getShowTime());

		Review review = Review.builder()
				.bookingId(1)
				.userId("ayj")
				.showInfoId(1)
				.showId(showId)
				.showName("제로베이스 수료식")
				.registerDate(LocalDate.parse("2024-05-10"))
				.text("리뷰입니다.")
				.build();

		reviewService.registerReview(review);
		Review resultedReview = reviewService.readMyReview(review);

	    // when
		reviewRepository.deleteById(resultedReview.getId());
		Review deletedReview = reviewService.readMyReview(review);

	    // then
		assertNull(deletedReview);
	}
}
