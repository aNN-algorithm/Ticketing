package zerobase.ticketing.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "booking")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private int bookingId;

    @Column(nullable = false, name = "seat_id")
    private int seatId;

    @Column(nullable = false, name = "show_info_id")
    private int showInfoId;

    @Column(nullable = false, name = "booked_date")
    private LocalDate bookedDate;

    @Column(nullable = false, name = "user_id")
    private String userId;

    @Column(nullable = false, name = "pay_status")
    private String payStatus;


}
