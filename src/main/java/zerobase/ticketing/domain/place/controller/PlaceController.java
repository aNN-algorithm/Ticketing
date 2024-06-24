package zerobase.ticketing.domain.place.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import zerobase.ticketing.domain.place.entity.Place;
import zerobase.ticketing.domain.place.service.PlaceService;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping("/register/place")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> registerPlace(@RequestBody List<Place> placeList) {
        // 공연장 등록하기

        int insertedCnt = placeService.registerPlace(placeList);
        return ResponseEntity.ok(insertedCnt); // 리스트 중 중복된 좌석 빼고 추가된 좌석 수 반환
    }

    @GetMapping("/read/place/venue")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Place> readPlace(@RequestParam("venue") String venue) {
        //이름으로 공연장 찾기

        return placeService.readPlace(venue);
    }

    @GetMapping("/update/placeVenue")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updatePlaceVenue(@RequestParam("preVenue") String preVenue,
                                 @RequestParam("newVenue") String newVenue) {
        // 공연장 이름 변경

        placeService.updatePlaceVenue(preVenue, newVenue);
    }

    @PostMapping("/update/placeSeat")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updatePlaceSeat(@RequestBody Place place) {
        // 공연장 좌석 변경
        // 실제 서비스 내에서는 이루어질 확률이 적지만(삭제 후 추가 방식이 사용될 거라고 예상),
        // 백엔드 기능 자체는 기구현

        placeService.updatePlaceSeat(place);
    }

    @PostMapping("/add/PlaceSeat")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addPlaceSeat(@RequestBody Place place) {
        // 공연장 좌석 추가

        placeService.addPlaceSeat(place);
    }

    @PostMapping("/delete/placeSeat")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deletePlaceSeat(@RequestBody Place place) {
        // 공연장 좌석 삭제

        placeService.deletePlaceSeat(place);
    }
}
