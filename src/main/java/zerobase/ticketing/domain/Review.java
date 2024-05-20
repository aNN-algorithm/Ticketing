package zerobase.ticketing.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "review")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "booking_id")
    private int bookingId;

    @Column(nullable = false, name = "user_id")
    private String userId;

    @Column(nullable = false, name = "register_date")
    private LocalDate registerDate;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false, name = "show_info_id")
    private int showInfoId;

    @Column(nullable = false, name = "showName")
    private String showName;

    @Column(nullable = false, name = "show_id")
    private int showId;
}