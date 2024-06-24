package zerobase.ticketing.domain.show.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class SoonOpenShow {
    int showInfoId;
    LocalDate openBooking;
    LocalTime openBookingTime;
    String showName;

    public SoonOpenShow(int showInfoId, LocalDate openBooking, LocalTime openBookingTime, String showName) {
        this.showInfoId = showInfoId;
        this.openBooking = openBooking;
        this.openBookingTime = openBookingTime;
        this.showName = showName;
    }
}
