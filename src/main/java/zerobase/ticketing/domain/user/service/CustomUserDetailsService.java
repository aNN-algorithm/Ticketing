package zerobase.ticketing.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zerobase.ticketing.domain.user.entity.CustomUserDetails;
import zerobase.ticketing.domain.user.entity.User;
import zerobase.ticketing.domain.user.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = memberRepository.findByUserId(userId);

        return new CustomUserDetails(user);
    }
}
