package zerobase.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.ticketing.domain.BlackList;

public interface BlackListRepository extends JpaRepository<BlackList, Integer> {
    Boolean existsByInvalidToken(String token);
}
