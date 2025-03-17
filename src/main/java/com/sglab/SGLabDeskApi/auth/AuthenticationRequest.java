package com.sglab.SGLabDeskApi.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    public String avatar;
    public String name;
    public String type;
    public String open_id;
    public String phone;
    public String username;
    public String email;
    public String password;
}
