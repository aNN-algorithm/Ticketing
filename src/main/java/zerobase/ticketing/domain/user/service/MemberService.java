package zerobase.ticketing.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zerobase.ticketing.domain.user.entity.Auth;
import zerobase.ticketing.domain.user.entity.BlackList;
import zerobase.ticketing.domain.user.entity.CustomUserDetails;
import zerobase.ticketing.domain.user.entity.User;
import zerobase.ticketing.domain.user.repository.BlackListRepository;
import zerobase.ticketing.domain.user.repository.MemberRepository;
import zerobase.ticketing.domain.user.security.JwtUtil;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final BlackListRepository blackListRepository;
    private final JwtUtil jwtUtil;

    public User register(Auth.SignUp member) {
        boolean isUser = memberRepository.existsByUserId(member.getUserId());
        if (isUser) {
            throw new RuntimeException("이미 사용중인 아이디입니다.");
        }

        // 비밀번호 해시처리
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member.toEntity());
    }

    public String login(Auth.SignIn member) {
        User user = memberRepository.findByUserId(member.getUserId());
        if (user == null) {
            throw new RuntimeException("존재하지 않는 계정입니다.");
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(user);

        return token;
    }

    public void logout(String refreshToken, Authentication authentication) {
        if (jwtUtil.validateToken(refreshToken)) {
            // Black_list 테이블에 해당 토큰 추가
            BlackList blackList = new BlackList();
            blackList.setInvalidToken(refreshToken);
            blackListRepository.save(blackList);
        }
    }

    public void deleteUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();

        memberRepository.deleteByUserId(userId);
    }
}
