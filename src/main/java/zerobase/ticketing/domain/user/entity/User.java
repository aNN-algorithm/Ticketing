package zerobase.ticketing.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Entity(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE user_id = ?")
@Where(clause = "deleted = false")
@Builder
public class User {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "user_name")
    private String userName;

    @Column(nullable = false, name = "user_email")
    private String userEmail;

    @Column(nullable = false, name = "user_mobile")
    private String userMobile;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false, name = "user_addr")
    private String userAddr;

    @Column(nullable = false)
    private boolean provision1;

    @Column(nullable = false)
    private boolean provision2;

    @Column(nullable = false)
    private boolean provision3;

    @Column(nullable = false)
    private boolean sms;

    @Column(nullable = false)
    private String roles;

    @Column(nullable = false)
    private boolean deleted = Boolean.FALSE;
}
