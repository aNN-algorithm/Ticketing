package zerobase.ticketing.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import zerobase.ticketing.domain.Booking;
import zerobase.ticketing.domain.CustomUserDetails;
import zerobase.ticketing.domain.Seat;
import zerobase.ticketing.domain.ShowInfo;
import zerobase.ticketing.repository.BookingRepository;
import zerobase.ticketing.repository.SeatRepository;
import zerobase.ticketing.repository.ShowInfoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final ShowInfoRepository showInfoRepository;

    public BookingService(BookingRepository bookingRepository, SeatRepository seatRepository
            , ShowInfoRepository showInfoRepository) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.showInfoRepository = showInfoRepository;
    }

    public Booking bookBooking(Booking booking, Authentication authentication) {
        // 티켓 예매하기

        // 이미 예매된 좌석인지 확인
        if (seatRepository.findBookedStatusBySeatId(booking.getSeatId()).equals("Booked")) {
            throw new RuntimeException("이미 예매된 좌석입니다.");
        }

        // API요청 안에 userId가 있을 수 있지만, 백엔드에서 장치 마련
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        booking.setUserId(userDetails.getUsername());

        // Seat 테이블의 해당 자리 예매 처리 (재고: 0)
        Seat newSeat = new Seat();
        newSeat.setBookingStatus("Booked");
        newSeat.setSeatId(booking.getSeatId());
        seatRepository.save(newSeat);

        // Show_info 테이블의 예매된 수 + 1
        ShowInfo showInfo = showInfoRepository.findByShowInfoId(booking.getShowInfoId());
        showInfo.setBookedSeat(showInfo.getBookedSeat() + 1);
        showInfoRepository.save(showInfo);

        // Booking 테이블에 Insert
        booking.setPayStatus("Booked");
        bookingRepository.save(booking);

        return booking;
    }

    public List<Booking> readBookingList(LocalDate startDate, LocalDate endDate, Authentication authentication) {
        // 로그인한 사람이 일정 기간 예매한 내역 확인

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();

        List<Booking> list = bookingRepository.findAllByUserIdAndBookedDateBetween(userId, startDate, endDate);

        return list;
    }

    public Optional<Booking> readBooking(int bookingId) {
        // 예매 번호로 예매한 내역 확인

        return bookingRepository.findById(bookingId);
    }

    public Booking findBooking(int seatId, int showInfoId, LocalDate bookedDate) {
        return bookingRepository.findBySeatIdAndShowInfoIdAndBookedDate(seatId, showInfoId, bookedDate);
    }

    public void cancelBooking(Booking booking, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();

        Optional<Booking> readBooking = bookingRepository.findById(booking.getBookingId());
        if (!userId.equals(readBooking.get().getUserId())) {
            throw new RuntimeException("비정상적인 접근입니다.");
        }

        // - Seat 테이블의 해당 자리 상태 변경
        Seat seat = new Seat();
        seat.setBookingStatus("NOT");
        seat.setSeatId(readBooking.get().getSeatId());
        seatRepository.save(seat);

        // - show_info 테이블에 예매된 수 감소
        ShowInfo showInfo = showInfoRepository.findByShowInfoId(readBooking.get().getShowInfoId());
        showInfo.setBookedSeat(showInfo.getBookedSeat() - 1);
        showInfoRepository.save(showInfo);

        // - Booking 테이블에서 해당 예매 내역 예매 취소로 변경
        readBooking.get().setPayStatus("Canceled");
        bookingRepository.save(readBooking.get());

    }

    public void deleteBooking(Booking booking) {
        bookingRepository.deleteById(booking.getBookingId());
    }
}
