package zerobase.ticketing.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

public class Auth {

    @Data
    public static class SignIn {
        private String userId;
        private String password;
    }

    @Data
    public static class SignUp {
        private String userId;
        private String password;
        private String userName;
        private String userEmail;
        private String userMobile;
        private LocalDate birth;
        private String userAddr;
        private boolean provision1;
        private boolean provision2;
        private boolean provision3;
        private boolean sms;
        private String roles;

        public User toEntity() {
            return User.builder()
                    .userId(this.userId)
                    .password(this.password)
                    .userName(this.userName)
                    .userEmail(this.userEmail)
                    .userMobile(this.userMobile)
                    .birth(this.birth)
                    .userAddr(this.userAddr)
                    .provision1(this.provision1)
                    .provision2(this.provision2)
                    .provision3(this.provision3)
                    .sms(this.sms)
                    .roles(this.roles)
                    .build();
        }
    }
}
