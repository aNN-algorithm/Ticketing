package zerobase.ticketing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.ticketing.domain.Seat;
import zerobase.ticketing.repository.SeatRepository;
import zerobase.ticketing.service.SeatService;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

@SpringBootTest
public class SeatTests {

    private final SeatService seatService;
    private final SeatRepository seatRepository;

    @Autowired
    public SeatTests(SeatService seatService, SeatRepository seatRepository) {
        this.seatService = seatService;
        this.seatRepository = seatRepository;
    }

    @Test
    @Transactional
    void registerSeat() {
        // given
        List<Seat> seatList = Arrays.asList(
                new Seat(1, 1, "1층", "A구역", "2열", "11~14", 150000, "NOT"),
                new Seat(1, 1, "1층", "A구역", "2열", "15~30", 150000, "NOT"),
                new Seat(1, 1, "1층", "A구역", "2열", "31~34", 150000, "NOT")
        );
        // 24좌석

        // when
        int insertedCount = seatService.registerSeat(seatList);

        // then
        assertEquals(24, insertedCount);
    }

    @Test
    @Transactional
    void readSeat() {
        // given
        List<Seat> seatList = Arrays.asList(
                new Seat(1, 1, "1층", "A구역", "2열", "11~14", 150000, "NOT"),
                new Seat(1, 1, "1층", "A구역", "2열", "15~30", 150000, "NOT"),
                new Seat(1, 1, "1층", "A구역", "2열", "31~34", 150000, "NOT")
        );
        int insertedCount = seatService.registerSeat(seatList);

        // when
        int readCount = seatService.readSeatList(1).size();

        // then
        assertEquals(24, readCount);
    }

    @Test
    @Transactional
    void updateSeat() {
        // given
        Seat seat = Seat.builder()
                .showId(1)
                .floor("1층")
                .sector("A구역")
                .line("0열")
                .seatNo("1~1")
                .price(150000)
                .bookingStatus("NOT").build();
        List<Seat> list = Arrays.asList(seat);
        seatService.registerSeat(list);

        seat.setSeatNo("1"); // 검색을 위한 셋팅
        int seatId = seatRepository.findSeatIdBySeat(seat);
        seat.setSeatId(seatId);
        seat.setPrice(200000);
        seatService.updateSeat(seat);

        // when
        Seat resultSeat = seatService.readSeat(seatId);

        // then
        assertEquals(200000, resultSeat.getPrice());
    }

    @Test
    @Transactional
    void deleteSeat() {
        // given
        Seat seat = Seat.builder()
                .showId(1)
                .floor("1층")
                .sector("A구역")
                .line("0열")
                .seatNo("1~1")
                .price(150000)
                .bookingStatus("NOT").build();
        List<Seat> list = Arrays.asList(seat);
        seatService.registerSeat(list);

        seat.setSeatNo("1"); // 검색을 위한 셋팅
        int seatId = seatRepository.findSeatIdBySeat(seat);

        // when
        seatService.deleteSeat(seatId);

        // then
        assertEquals(false, seatService.existsBySeat(seat));
    }
}
