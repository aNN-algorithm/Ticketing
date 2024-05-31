package zerobase.ticketing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.ticketing.domain.ShowInfo;
import zerobase.ticketing.repository.ShowInfoRepository;

import java.time.LocalDate;

import static junit.framework.TestCase.assertEquals;


@SpringBootTest
class ShowInfoTests {

    private final ShowInfoRepository showInfoRepository;

    @Autowired
    public ShowInfoTests(ShowInfoRepository showInfoRepository) {
        this.showInfoRepository = showInfoRepository;
    }

    @Test
    @Transactional
    public void registerAndReadShowInfo() {
        // Create
        // Read
        // 테스트

        // given
        ShowInfo newShowInfo = ShowInfo.builder()
                .showName("제로베이스 수료식")
                .startDate(LocalDate.parse("2024-07-01"))
                .endDate(LocalDate.parse("2024-07-01"))
                .venue("온라인")
                .runTime(120)
                .ageGroup(20)
                .genre("Event")
                .keyword("백엔드")
                .cast("수료생")
                .bookedSeat(0)
                .allSeat(200)
                .managerId("ayj").build();

        // when
        showInfoRepository.save(newShowInfo);
        int showInfoId = showInfoRepository.findByShowNameAndStartDateAndEndDate
                (newShowInfo.getShowName(), newShowInfo.getStartDate(), newShowInfo.getEndDate()).get(0).getShowInfoId();

        // then
        assertEquals(newShowInfo.getShowName(), showInfoRepository.findByShowInfoId(showInfoId).getShowName());
    }

    @Test
    @Transactional
    void updateShowInfo() {
        // Update
        // 테스트

        // given
        ShowInfo newShowInfo = ShowInfo.builder()
                .showName("제로베이스 수료식")
                .startDate(LocalDate.parse("2024-07-01"))
                .endDate(LocalDate.parse("2024-07-01"))
                .venue("온라인")
                .runTime(120)
                .ageGroup(20)
                .genre("Event")
                .keyword("백엔드")
                .cast("수료생")
                .bookedSeat(0)
                .allSeat(200)
                .managerId("ayj").build();
        showInfoRepository.save(newShowInfo);
        int showInfoId = showInfoRepository.findByShowNameAndStartDateAndEndDate
                (newShowInfo.getShowName(), newShowInfo.getStartDate(), newShowInfo.getEndDate()).get(0).getShowInfoId();

        // when
        newShowInfo.setShowName("제로베이스 축하 수료식");
        showInfoRepository.save(newShowInfo);

        // then
        assertEquals("제로베이스 축하 수료식", showInfoRepository.findByShowInfoId(showInfoId).getShowName());
    }

    @Test
    @Transactional
    void deleteShowInfo() {
        // Delete
        // 테스트

        // given
        ShowInfo newShowInfo = ShowInfo.builder()
                .showName("제로베이스 수료식")
                .startDate(LocalDate.parse("2024-07-01"))
                .endDate(LocalDate.parse("2024-07-01"))
                .venue("온라인")
                .runTime(120)
                .ageGroup(20)
                .genre("Event")
                .keyword("백엔드")
                .cast("수료생")
                .bookedSeat(0)
                .allSeat(200)
                .managerId("ayj").build();
        showInfoRepository.save(newShowInfo);
        int showInfoId = showInfoRepository.findByShowNameAndStartDateAndEndDate
                (newShowInfo.getShowName(), newShowInfo.getStartDate(), newShowInfo.getEndDate()).get(0).getShowInfoId();

        // when
        showInfoRepository.deleteByShowInfoId(showInfoId);

        // then
        assertEquals(false, showInfoRepository.existsByShowInfoId(showInfoId));
    }

}
