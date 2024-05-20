package zerobase.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import zerobase.ticketing.domain.User;

public interface MemberRepository extends JpaRepository<User, Integer> {
    boolean existsByUserId(String userId);

    User findByUserId(String userId);

    @Transactional
    void deleteByUserId(String userId);
}
