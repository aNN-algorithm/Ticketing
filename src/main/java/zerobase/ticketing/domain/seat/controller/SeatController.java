package zerobase.ticketing.domain.seat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import zerobase.ticketing.domain.user.entity.Seat;
import zerobase.ticketing.domain.seat.service.SeatService;

import java.util.List;

@RestController
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PostMapping("/register/seat")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> registerSeat(@RequestBody List<Seat> seatList) {
        // 좌석 등록

        int insertedCount = seatService.registerSeat(seatList);
        return ResponseEntity.ok(insertedCount);
    }

    @GetMapping("/read/seatList")
    public List<Seat> readSeatList(@RequestParam("showId") int showId) {
        // 회차당 좌석 리스트 조회

        return seatService.readSeatList(showId);
    }

    @GetMapping("/read/seat")
    public Seat readSeat(@RequestParam("seatId") int seatId) {
        // 자리 정보 조회

        return seatService.readSeat(seatId);
    }

    @PostMapping("/update/seat")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void updateSeat(@RequestBody Seat seat) {
        // 자리 정보 변경

        seatService.updateSeat(seat);
    }

    @DeleteMapping("/delete/seat")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void deleteSeat(int seatId) {
        // 자리 정보 삭제

        seatService.deleteSeat(seatId);
    }

    @PostMapping("/add/seat")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void addSeat(@RequestBody Seat seat) {
        // 자리 정보 추가

        seatService.addSeat(seat);
    }
}
