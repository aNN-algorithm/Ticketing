package zerobase.ticketing.domain.place.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "place")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String venue;
    private String floor;
    private String sector;
    private String line;

    @Column(name = "seat_no")
    private String seatNo;
}
