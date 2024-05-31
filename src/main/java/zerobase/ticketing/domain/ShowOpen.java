package zerobase.ticketing.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "show_open")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowOpen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_id")
    int showId;

    @Column(nullable = false, name = "show_info_id")
    int showInfoId;

    @Column(nullable = false, name = "show_date")
    LocalDate showDate;

    @Column(nullable = false, name = "show_time")
    LocalTime showTime;

    @Column(nullable = false, name = "open_booking")
    LocalDate openBooking;

    @Column(nullable = false, name = "open_booking_time")
    LocalTime openBookingTime;

    @Column(nullable = false, name = "close_booking")
    LocalDate closeBooking;

    @Column(nullable = false, name = "close_booking_time")
    LocalTime closeBookingTime;

    String cast;
}
