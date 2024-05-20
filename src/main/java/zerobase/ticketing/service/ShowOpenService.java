package zerobase.ticketing.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import zerobase.ticketing.domain.CustomUserDetails;
import zerobase.ticketing.domain.ShowInfo;
import zerobase.ticketing.domain.ShowOpen;
import zerobase.ticketing.model.SoonOpenShow;
import zerobase.ticketing.repository.ShowInfoRepository;
import zerobase.ticketing.repository.ShowOpenRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShowOpenService {

    ShowOpenRepository showOpenRepository;
    ShowInfoRepository showInfoRepository;

    public ShowOpenService(ShowOpenRepository showOpenRepository, ShowInfoRepository showInfoRepository) {
        this.showOpenRepository = showOpenRepository;
        this.showInfoRepository = showInfoRepository;
    }

    public int registerShowOpen(List<ShowOpen> showOpenList) {
        // 회차 정보 등록

        List<ShowOpen> list = new ArrayList<>();

        for (int i = 0; i < showOpenList.size(); i++) {
            ShowOpen curShowOpen = showOpenList.get(i);

            // 해당 공연이 이미 등록되었는지 확인
            if (showOpenRepository.existsShowOpen(curShowOpen.getShowInfoId(),
                    curShowOpen.getShowDate(),
                    curShowOpen.getShowTime()) > 0) {
                throw new RuntimeException("이미 등록된 공연입니다. 요청하신 공연 등록을 실패하였습니다.");
            }

            list.add(curShowOpen);
        }

        try {
            showOpenRepository.batchInsert(list);
            return list.size();
        } catch (Exception e) {
            throw new RuntimeException("공연 회차 등록에 실패하였습니다.");
        }
    }

    public List<ShowOpen> readShowOpenList(int showInfoId) {
        // 해당 공연의 회차 리스트 조회

        return showOpenRepository.findShowOpenListByShowInfoId(showInfoId);
    }

    public ShowOpen readShowOpen(int showId) {
        // 해당 회차의 정보 조회

        return showOpenRepository.findShowByShowId(showId);
    }

    public void updateShowOpen(ShowOpen showOpen, Authentication authentication) {
        // 해당 회차를 수정하는 것이므로,
        // showId 를 포함하여 변경할 정보를 담아 요청

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String registeredUserId = showInfoRepository.findByShowInfoId(showOpen.getShowInfoId()).getManagerId();

        if (!registeredUserId.equals(userDetails.getUsername())) {
            throw new RuntimeException("수정할 권한이 없습니다.");
        }

        try {
            showOpenRepository.updateShowOpen(showOpen);
        } catch (Exception e) {
            throw new RuntimeException("수정에 실패하였습니다.");
        }
    }

    public void deleteShowOpen(ShowOpen showOpen, Authentication authentication) {
        // 해당 회차 삭제

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String registeredUserId = showInfoRepository.findByShowInfoId(showOpen.getShowInfoId()).getManagerId();

        if (!registeredUserId.equals(userDetails.getUsername())) {
            throw new RuntimeException("삭제할 권한이 없습니다.");
        }

        try {
            showOpenRepository.deleteShowOpen(showOpen);
        } catch (Exception e) {
            throw new RuntimeException("삭제에 실패하였습니다.");
        }
    }

    public List<ShowInfo> readPopularShow(String genre, LocalDate startDate, LocalDate endDate) {
        // 일정 기간 인기 많은 공연 조회

        return showOpenRepository.readPopularShow(genre, startDate, endDate);
    }

    public List<SoonOpenShow> readSoonOpenShow(LocalDate startDate, LocalDate endDate) {
        // 일정 기간 예매 임박한 공연 조회

        return showOpenRepository.readSoonOpenShow(startDate, endDate);
    }
}
