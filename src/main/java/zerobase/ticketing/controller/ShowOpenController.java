package zerobase.ticketing.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zerobase.ticketing.domain.ShowInfo;
import zerobase.ticketing.domain.ShowOpen;
import zerobase.ticketing.model.SoonOpenShow;
import zerobase.ticketing.service.ShowOpenService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ShowOpenController {

    ShowOpenService showOpenService;


    public ShowOpenController(ShowOpenService showOpenService) {
        this.showOpenService = showOpenService;
    }

    @PostMapping("/register/showOpen")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> registerShowOpen(@RequestBody List<ShowOpen> showOpenList) {
        // 회차 정보 등록

        int insertedCnt = showOpenService.registerShowOpen(showOpenList);
        return ResponseEntity.ok(insertedCnt);
    }

    @GetMapping("/read/showOpenList")
    public List<ShowOpen> readShowOpenList(@RequestParam("showInfoId") int showInfoId) {
        // 해당 공연의 회차 리스트 조회

        return showOpenService.readShowOpenList(showInfoId);
    }

    @GetMapping("/read/showOpen")
    public ShowOpen readShowOpen(@RequestParam("showId") int showId) {
        // 해당 회차의 정보 조회

        return showOpenService.readShowOpen(showId);
    }

    @PostMapping("/update/showOpen")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void updateShowOpen(@RequestBody ShowOpen showOpen,
                               @Validated Authentication authentication) {
        // 해당 회차를 수정하는 것이므로,
        // showId 를 포함하여 변경할 정보를 담아 요청

        try {
            showOpenService.updateShowOpen(showOpen, authentication);
        } catch (Exception e) {
            throw new RuntimeException("수정에 실패하였습니다.");
        }
    }

    @PostMapping("/delete/showOpen")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void deleteShowOpen(@RequestBody ShowOpen showOpen,
                               @Validated Authentication authentication) {
        // 해당 회차 삭제

        try {
            showOpenService.deleteShowOpen(showOpen, authentication);
        } catch (Exception e) {
            throw new RuntimeException("삭제에 실패하였습니다.");
        }
    }

    @GetMapping("/read/popularShow")
    public List<ShowInfo> readPopularShow(@RequestBody String genre,
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        // 일정 기간 인기 많은 공연 조회

        return showOpenService.readPopularShow(genre, startDate, endDate);
    }

    @PostMapping("/read/openShow")
    public List<SoonOpenShow> readOpenShow(@RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        // 일정 기간 예매 임박한 공연 조회

        return showOpenService.readSoonOpenShow(startDate, endDate);
    }
}
