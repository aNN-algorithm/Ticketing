package zerobase.ticketing.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import zerobase.ticketing.domain.CustomUserDetails;
import zerobase.ticketing.domain.ShowInfo;
import zerobase.ticketing.repository.PlaceRepository;
import zerobase.ticketing.repository.ShowInfoRepository;

import java.util.List;

@Service
public class ShowInfoService {

    private final ShowInfoRepository showInfoRepository;
    private final PlaceRepository placeRepository;

    public ShowInfoService(ShowInfoRepository showInfoRepository, PlaceRepository placeRepository) {
        this.showInfoRepository = showInfoRepository;
        this.placeRepository = placeRepository;
    }

    public void registerShowInfo(ShowInfo showInfo, Authentication authentication) {
        // 로그인한 정보를 통해 공연 개요 등록

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        showInfo.setManagerId(userDetails.getUsername());

        boolean isVenue = placeRepository.existsByName(showInfo.getVenue());
        if (!isVenue) {
            throw new RuntimeException("등록되지 않은 공연장입니다.");
        } else {
            showInfoRepository.save(showInfo);
        }
    }

    public ShowInfo readShowInfo(int showInfoId) {
        // showInfoId(공연 개요 Id)로 해당 공연 개요 불러오기

        return showInfoRepository.findByShowInfoId(showInfoId);
    }

    public List<ShowInfo> searchShowName(String showName) {
        // 공연명 검색

        return showInfoRepository.findAllByShowNameContaining(showName);
    }

    public ShowInfo updateShowInfo(ShowInfo showInfo, Authentication authentication) {
        // showInfoId(공연 개요 Id)를 포함하여 변경할 정보를 담아 요청

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (!showInfo.getManagerId().equals(userDetails.getUsername())) {
            throw new RuntimeException("해당 공연에 접근 권한이 없습니다.");
        }

        try {
            showInfoRepository.save(showInfo);
            return showInfo;
        } catch (Exception e) {
            throw new RuntimeException("수정에 실패하였습니다.");
        }
    }

    public void deleteShowInfo(int showInfoId, Authentication authentication) {
        // showInfoId(공연 개요 Id)를 가지고 공연 개요 삭제

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (!showInfoRepository.findByShowInfoId(showInfoId).getManagerId().equals(userDetails.getUsername()))
        {
            throw new RuntimeException("해당 공연 삭제 권한이 없습니다.");
        }

        try {
            showInfoRepository.deleteByShowInfoId(showInfoId);
        } catch (Exception e) {
            throw new RuntimeException("삭제에 실패하였습니다.");
        }
    }
}
