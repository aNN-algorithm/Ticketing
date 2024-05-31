package zerobase.ticketing.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seatId;

    @Column(nullable = false, name = "show_id")
    private int showId;

    private String floor;
    private String sector;
    private String line;

    @Column(name = "seat_no")
    private String seatNo;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, name = "booking_status")
    private String bookingStatus;
}
