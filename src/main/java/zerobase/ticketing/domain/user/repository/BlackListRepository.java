package zerobase.ticketing.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.ticketing.domain.user.entity.BlackList;

public interface BlackListRepository extends JpaRepository<BlackList, Integer> {
    Boolean existsByInvalidToken(String token);
}
