package zerobase.ticketing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zerobase.ticketing.domain.Auth;
import zerobase.ticketing.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Auth.SignUp request) {
        // 회원 가입

        return ResponseEntity.ok(memberService.register(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn request) {
        // 로그인

        String token = memberService.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/signout")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<?> signout(@RequestHeader("Refresh") String refreshToken,
                                     @Validated Authentication authentication) {
        // 로그아웃

        memberService.logout(refreshToken, authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteUser(@Validated Authentication authentication) {
        // 회원 탈퇴(Soft delete)

        memberService.deleteUser(authentication);
        return ResponseEntity.noContent().build();
    }
}

