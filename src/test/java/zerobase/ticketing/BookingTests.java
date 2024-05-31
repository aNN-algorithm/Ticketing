package zerobase.ticketing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.ticketing.domain.Booking;
import zerobase.ticketing.repository.BookingRepository;
import zerobase.ticketing.service.BookingService;

import java.time.LocalDate;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

@SpringBootTest
public class BookingTests {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingTests(BookingService bookingService, BookingRepository bookingRepository) {
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
    }

    @Test
    @Transactional
    void bookBooking() {
        // given
        Booking booking = Booking.builder()
                .seatId(1)
                .showInfoId(1)
                .bookedDate(LocalDate.parse("2024-05-20"))
                .userId("ayj")
                .payStatus("Booked")
                .build();
        bookingRepository.save(booking);

        // when
        Booking resultBooking = bookingService.findBooking(booking.getSeatId()
                , booking.getShowInfoId(), booking.getBookedDate());

        // then
        assertEquals(booking.getSeatId(), resultBooking.getSeatId());
        assertEquals(booking.getUserId(), resultBooking.getUserId());
    }

    @Test
    @Transactional
    void readBooking() {
        // given
        Booking booking = Booking.builder()
                .seatId(1)
                .showInfoId(1)
                .bookedDate(LocalDate.parse("2024-05-20"))
                .userId("ayj")
                .payStatus("Booked")
                .build();
        bookingRepository.save(booking);
        Booking resultBooking = bookingService.findBooking(booking.getSeatId()
                , booking.getShowInfoId(), booking.getBookedDate());

        // when
        List<Booking> list = bookingRepository.findAllByUserIdAndBookedDateBetween("ayj",
                LocalDate.parse("2024-05-01"), LocalDate.parse("2024-05-31"));

        // then
        assertEquals(1, list.size());
    }

    @Test
    @Transactional
    void cancelBooking() {
        // given
        Booking booking = Booking.builder()
                .seatId(1)
                .showInfoId(1)
                .bookedDate(LocalDate.parse("2024-05-20"))
                .userId("ayj")
                .payStatus("Booked")
                .build();
        bookingRepository.save(booking);

        // when
        booking.setPayStatus("Canceled");
        bookingRepository.save(booking);
        Booking resultBooking = bookingService.findBooking(booking.getSeatId()
                , booking.getShowInfoId(), booking.getBookedDate());

        // then
        assertEquals("Canceled", resultBooking.getPayStatus());
    }

    @Test
    @Transactional
    void deleteBooking() {
        // given
        Booking booking = Booking.builder()
                .seatId(1)
                .showInfoId(1)
                .bookedDate(LocalDate.parse("2024-05-20"))
                .userId("ayj")
                .payStatus("Booked")
                .build();
        bookingRepository.save(booking);
        Booking insertBooking = bookingService.findBooking(booking.getSeatId()
                , booking.getShowInfoId(), booking.getBookedDate());

        // when
        bookingService.deleteBooking(insertBooking);
        Booking resultBooking = bookingService.findBooking(booking.getSeatId()
                , booking.getShowInfoId(), booking.getBookedDate());

        // then
        assertNull(resultBooking);
    }
}
