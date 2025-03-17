package com.sglab.SGLabDeskApi.auth;

import com.sglab.SGLabDeskApi.users.UsersEntity;
import com.sglab.SGLabDeskApi.utils.ResponseBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse extends ResponseBase implements Serializable {
    private String token;
    private UsersEntity user;
}

