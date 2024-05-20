package zerobase.ticketing.service;

import org.springframework.stereotype.Service;
import zerobase.ticketing.domain.Seat;
import zerobase.ticketing.repository.SeatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public int registerSeat(List<Seat> seatList) {
        // 좌석 등록

        List<Seat> list = new ArrayList<>();

        for (int i = 0; i < seatList.size(); i++) {
            Seat curSeat = seatList.get(i);

            StringTokenizer st = new StringTokenizer(curSeat.getSeatNo(), "~");
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());

            for (int j = start; j <= end; j++) {
                if (seatRepository.existsBySeat(curSeat.getShowId(), curSeat.getFloor(), curSeat.getSector(),
                        curSeat.getLine(), Integer.toString(j), curSeat.getPrice()) > 0) {
                    System.out.println("해당 공연에 이미 등록된 좌석입니다.");
                    continue;
                }

                Seat newSeat = Seat.builder()
                        .showId(curSeat.getShowId())
                        .floor(curSeat.getFloor())
                        .sector(curSeat.getSector())
                        .line(curSeat.getLine())
                        .seatNo(Integer.toString(j))
                        .price(curSeat.getPrice())
                        .bookingStatus("NOT")
                        .build();
                list.add(newSeat);
            }
        }

        try {
            seatRepository.batchInsert(list);
            return list.size();
        } catch (Exception e) {
            throw new RuntimeException("좌석 등록에 실패하였습니다.");
        }
    }

    public List<Seat> readSeatList(int showId) {
        // 회차당 좌석 리스트 조회

        return seatRepository.findSeatListByShowId(showId);
    }

    public Seat readSeat(int seatId) {
        // 자리 정보 조회

        return seatRepository.findSeatBySeatId(seatId);
    }

    public boolean existsBySeat(Seat seat) {
        // 중복된 좌석이 있는지 확인

        return seatRepository.existsBySeat(seat.getShowId(), seat.getFloor(), seat.getSector(),
                seat.getLine(), seat.getSeatNo(), seat.getPrice()) > 0;
    }

    public void updateSeat(Seat seat) {
        // 자리 정보 변경

        // 변경될 좌석이 이미 있는지 확인
        if (existsBySeat(seat)) {
            throw new RuntimeException("해당 회차에 해당 자리가 존재합니다.");
        }

        seatRepository.updateSeat(seat);
    }

    public void deleteSeat(int seatId) {
        // 자리 정보 삭제

        seatRepository.deleteSeat(seatId);
    }

    public void addSeat(Seat seat) {
        // 자리 정보 추가

        if (existsBySeat(seat)) {
            throw new RuntimeException("해당 회차에 해당 자리가 존재합니다.");
        }

        seatRepository.addSeat(seat);
    }
}
