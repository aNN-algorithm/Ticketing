package zerobase.ticketing.domain.show.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "show_info")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowInfo {
    // 공연 개요

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_info_id")
    private int showInfoId;

    @Column(nullable = false, name = "show_name")
    private String showName;

    @Column(nullable = false, name = "start_date")
    private LocalDate startDate;

    @Column(nullable = false, name = "end_date")
    private LocalDate endDate;

    @Column(nullable = false)
    private String venue;

    @Column(nullable = false, name = "run_time")
    private int runTime;

    @Column(nullable = false, name = "age_group")
    private int ageGroup;

    @Column(nullable = false)
    private String genre;

    private String keyword;
    private String cast;

    @Column(nullable = false, name = "booked_seat")
    private int bookedSeat;

    @Column(nullable = false, name = "all_seat")
    private int allSeat;

    @Column(nullable = false, name = "manager_id")
    private String managerId;

}
