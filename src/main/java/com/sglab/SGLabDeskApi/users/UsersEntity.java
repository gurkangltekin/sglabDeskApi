package com.sglab.SGLabDeskApi.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sglab.SGLabDeskApi.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Data
@SuperBuilder // Builder kalıtım desteği
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Getter
@Setter
public class UsersEntity extends BaseEntity implements UserDetails {


    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "is_enabled")
    @JsonIgnore
    private boolean isEnabled;

    @Column(name = "email_verified_date")
    @JsonIgnore
    private LocalDateTime emailVerifiedDate;

    @Column(name = "remember_token")
    private String rememberToken;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "description")
    private String description;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;

    @Column(name = "fcmtoken")
    private String fcmToken;

    @Column(name = "online")
    private Boolean online;

    @Column(name = "open_id")
    private String openId;

    @Column(name = "token")
    private String token;

    @Setter
    @Column(name = "type")
    private int type;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    public UsersEntity(boolean isDeleted, LocalDateTime version, LocalDateTime createdDate, String username, String email, String name, String openId, int type, Boolean isEnabled) {
        super(isDeleted, version, createdDate);
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isEnabled = isEnabled;
        this.openId = openId;
        this.type = type;

    }

    public UsersEntity(boolean isDeleted, LocalDateTime version, LocalDateTime createdDate, String username, String email, String password, String name, Boolean isEnabled) {
        super(isDeleted, version, createdDate);
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isEnabled = isEnabled;
    }

    public UsersEntity(UUID id, boolean isDeleted, LocalDateTime version, LocalDateTime createdDate, String username, String email, String password, String name, boolean isEnabled, LocalDateTime emailVerifiedDate, String rememberToken, String accessToken, String avatar, String description, LocalDateTime expireDate, String cfmToken, Boolean online, String openId, String token, int type) {
        super(id, isDeleted, version, createdDate);
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.isEnabled = isEnabled;
        this.emailVerifiedDate = emailVerifiedDate;
        this.rememberToken = rememberToken;
        this.accessToken = accessToken;
        this.avatar = avatar;
        this.description = description;
        this.expireDate = expireDate;
        this.fcmToken = cfmToken;
        this.online = online;
        this.openId = openId;
        this.token = token;
        this.type = type;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(getUsername());
        return Collections.singletonList(authority);
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean Enabled() {return isEnabled;}

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }


}
