package zerobase.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.ticketing.domain.Booking;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByUserIdAndBookedDateBetween(String userId, LocalDate startDate, LocalDate endDate);

    Booking findBySeatIdAndShowInfoIdAndBookedDate(int seatId, int showInfoId, LocalDate bookedDate);
}
