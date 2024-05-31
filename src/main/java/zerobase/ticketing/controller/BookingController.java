package zerobase.ticketing.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zerobase.ticketing.domain.Booking;
import zerobase.ticketing.service.BookingService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book/booking")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> bookBooking(@RequestBody Booking booking,
                                         @Validated Authentication authentication) {
        // 티켓 예매하기
        // Service
        // - 이미 예매된 좌석인지 확인
        // - Seat 테이블의 해당 자리 상태 변경(예매 처리)
        // - show_info 테이블에 예매된 수 증가
        // - Booking 테이블에 Insert

        Booking newBooking = bookingService.bookBooking(booking, authentication);
        return ResponseEntity.ok(newBooking);
    }

    @GetMapping("/read/bookingList")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Booking> readBookingList(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                         @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                         @Validated Authentication authentication) {
        // 로그인한 사람이 일정 기간 예매한 내역 확인

        return bookingService.readBookingList(startDate, endDate, authentication);
    }

    @GetMapping("/read/booking")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Optional<Booking> readBooking(@RequestParam("bookId") int bookId) {
        // 예매 번호로 예매한 내역 확인

        return bookingService.readBooking(bookId);
    }

    @PostMapping("/cancel/booking")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void cancelBooking(@RequestBody Booking booking,
                              @Validated Authentication authentication) {
        // 예매 취소하기
        // Service
        // - 사용자인지 확인
        // - Seat 테이블의 해당 자리 상태 변경
        // - show_info 테이블에 예매된 수 감소
        // - Booking 테이블에서 해당 예매 내역 예매 취소로 변경
        bookingService.cancelBooking(booking, authentication);
    }

    @PostMapping("/delete/booking")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteBooking(@RequestBody Booking booking) {
        // 서비스에서는 사용될 용도가 없으나 백엔드에서 구현

        bookingService.deleteBooking(booking);
    }
}
