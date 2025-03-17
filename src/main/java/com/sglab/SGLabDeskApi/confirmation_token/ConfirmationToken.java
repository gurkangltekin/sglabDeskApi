package com.sglab.SGLabDeskApi.confirmation_token;

import com.sglab.SGLabDeskApi.users.UsersEntity;
import com.sglab.SGLabDeskApi.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken extends BaseEntity {

    @Column(name = "token", nullable = false)
    private String token;
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private UsersEntity userEntity;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, UsersEntity userEntity) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.setCreatedDate(createdAt);
        this.userEntity = userEntity;
    }
}
