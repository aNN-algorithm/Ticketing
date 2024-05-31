package zerobase.ticketing.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zerobase.ticketing.domain.ShowInfo;
import zerobase.ticketing.service.ShowInfoService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ShowInfoController {

    private final ShowInfoService showInfoService;

    @PostMapping("/register/showInfo") // 많은 정보를 받을 것이니까 POST
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void registerShowInfo(@RequestBody ShowInfo showInfo,
                                 @Validated Authentication authentication) {
        // 로그인한 정보를 통해 공연 개요 등록

        showInfoService.registerShowInfo(showInfo, authentication);
    }

    @GetMapping("/read/showInfo")
    public ShowInfo readShowInfo(@RequestParam("showInfoId") int showInfoId) {
        // showInfoId(공연 개요 Id)로 해당 공연 개요 불러오기

        return showInfoService.readShowInfo(showInfoId);
    }

    @GetMapping("/search/showInfo")
    public List<ShowInfo> searchShowInfo(@RequestParam("showName") String showName) {
        // 공연명 검색

        return showInfoService.searchShowName(showName);
    }

    @PostMapping("/update/showInfo")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void updateShowInfo(@RequestBody ShowInfo showInfo,
                                            @Validated Authentication authentication){
        // 프론트: 해당 공연을 불러오기 등으로 선택하여, 해당 공연을 수정 요청
        // showInfoId(공연 개요 Id)를 포함하여 변경할 정보를 담아 요청

        showInfoService.updateShowInfo(showInfo, authentication);
    }

    @DeleteMapping("/delete/showInfo")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void deleteShowInfo(@RequestParam("showInfoId") int showInfoId,
                               @Validated Authentication authentication) {
        // showInfoId(공연 개요 Id)를 가지고 공연 개요 삭제

        showInfoService.deleteShowInfo(showInfoId, authentication);
    }
}
