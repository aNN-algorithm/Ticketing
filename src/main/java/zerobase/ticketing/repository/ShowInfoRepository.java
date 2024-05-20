package zerobase.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.ticketing.domain.ShowInfo;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShowInfoRepository extends JpaRepository<ShowInfo, Integer> {

    boolean existsByShowInfoId(int showInfoId);

    ShowInfo findByShowInfoId(int showInfoId);

    void deleteByShowInfoId(int showInfoId);

    List<ShowInfo> findAllByShowNameContaining(String showName);

    List<ShowInfo> findByShowNameAndStartDateAndEndDate(String showName, LocalDate startDate, LocalDate endDate);

}
