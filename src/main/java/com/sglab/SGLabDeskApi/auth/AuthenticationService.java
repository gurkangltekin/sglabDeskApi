package com.sglab.SGLabDeskApi.auth;

import com.sglab.SGLabDeskApi.confirmation_token.ConfirmationToken;
import com.sglab.SGLabDeskApi.confirmation_token.ConfirmationTokenService;
import com.sglab.SGLabDeskApi.utils.JwtService;
import com.sglab.SGLabDeskApi.users.IUsersRepository;
import com.sglab.SGLabDeskApi.users.Role;
import com.sglab.SGLabDeskApi.users.UsersEntity;
import com.sglab.SGLabDeskApi.users.UsersService;
import com.sglab.SGLabDeskApi.utils.ErrorKeys;
import com.sglab.SGLabDeskApi.utils.ErrorMessages;
import com.sglab.SGLabDeskApi.utils.ReturnCodes;
import com.sglab.SGLabDeskApi.utils.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final IUsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UsersService userService;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ConfirmationTokenService confirmationTokenService;
    private final AuthenticationManager authenticationManager;

    public ServiceResult<AuthenticationResponse> register(RegisterRequest request) {

        var existsUser = userRepository.findByUsername(request.getUsername());
        if(existsUser.isPresent()){
            return ServiceResult
                    .<AuthenticationResponse>builder()
                    .data(null)
                    .errorKey(ErrorKeys.USER_ALREADY_EXIST)
                    .success(false)
                    .code(-1)
                    .build();
        }
        else{
            var user = UsersEntity.builder()
                    .name(request.getName())
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();

            userRepository.save(user);

            var jwtToken = jwtService.generateToken(user);

            return ServiceResult
                    .<AuthenticationResponse>builder()
                    .data(AuthenticationResponse
                            .builder()
                            .token(jwtToken)
                            .successful(true)
                            .code(ReturnCodes.USER_REGISTER_SUCCESSFUL)
                            .user(user)
                            .build())
                    .success(true)
                    .code(1)
                    .build();
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            var user = userRepository
                    .findByUsername(request.getUsername())
                    .orElseThrow();

            if(!user.Enabled()){
                return AuthenticationResponse
                        .builder()
                        .message(ErrorMessages.USER_IS_NOT_ENABLED)
                        .code(ReturnCodes.USER_IS_NOT_ENABLED)
                        .successful(false)
                        .build();
            }

            var jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse
                    .builder()
                    .token(jwtToken)
                    .user(user)
                    .successful(true)
                    .code(ReturnCodes.USER_LOGIN_SUCCESSFUL)
                    .build();
        }
        catch (Exception ex){
            return AuthenticationResponse
                    .builder()
                    .user(null)
                    .message(ErrorMessages.USERNAME_OR_PASSWORD_IS_WRONG)
                    .code(ReturnCodes.USERNAME_OR_PASSWORD_IS_WRONG)
                    .successful(false)
                    .build();
        }
    }

    public AuthenticationResponse authenticateWithThirdParts(AuthenticationRequest request) {
        try{

            UsersEntity user = null;

            var userIsExists = userRepository
                    .findByUsername(request.getEmail().split("@")[0]);

            if(userIsExists.isEmpty()){
                user = UsersEntity.builder()
                        .name(request.getName())
                        .username(request.getEmail().split("@")[0])
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getOpen_id()))
                        .role(Role.USER)
                        .version(LocalDateTime.now())
                        .createdDate(LocalDateTime.now())
                        .avatar(request.getAvatar())
                        //.type(Integer.getInteger(request.getType()))
                        .build();

                userRepository.save(user);

                return AuthenticationResponse
                        .builder()
                        .message(ErrorMessages.USER_IS_NOT_ENABLED)
                        .code(ReturnCodes.USER_IS_NOT_ENABLED)
                        .successful(false)
                        .build();
            } else {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail().split("@")[0],
                                request.getOpen_id()
                        )
                );

                user = userRepository
                        .findByUsername(request.getEmail().split("@")[0])
                        .orElseThrow();

                if(!user.Enabled()){
                    return AuthenticationResponse
                            .builder()
                            .message(ErrorMessages.USER_IS_NOT_ENABLED)
                            .code(ReturnCodes.USER_IS_NOT_ENABLED)
                            .successful(false)
                            .build();
                }
            }



            var jwtToken = jwtService.generateToken(user);

            user.setAccessToken(jwtToken);
            confirmationTokenService.setExpiresAt(user.getId());

            confirmationTokenService.saveConfirmationToken(new ConfirmationToken(jwtToken,LocalDateTime.now(),null, user));

            return AuthenticationResponse
                    .builder()
                    .token(jwtToken)
                    .user(user)
                    .successful(true)
                    .code(ReturnCodes.USER_LOGIN_SUCCESSFUL)
                    .build();
        }
        catch (Exception ex){
            return AuthenticationResponse
                    .builder()
                    .user(null)
                    .message(ex.getMessage())
                    .code(ReturnCodes.SOMETHING_IS_WRONG)
                    .successful(false)
                    .build();
        }
    }

    public AuthenticationResponse controlTokenIsExpired(String token) {
        try{
            String userName = jwtService.extractUsername(token);

            Optional<UsersEntity> user = userRepository.findByUsername(userName);
            UserDetails userDetail = userDetailsService.loadUserByUsername(userName);

            if(user.isEmpty()){
                return AuthenticationResponse
                        .builder()
                        .user(null)
                        .message(ErrorMessages.USER_NOT_FOUND)
                        .code(ReturnCodes.USER_NOT_FOUND)
                        .successful(false)
                        .build();
            }

            boolean isTokenValid = jwtService.isTokenValid(token, userDetail);

            if(isTokenValid){
                return AuthenticationResponse
                        .builder()
                        .user(null)
                        .message(ErrorMessages.TOKEN_IS_VALID)
                        .code(ReturnCodes.TOKEN_IS_VALID)
                        .successful(true)
                        .build();
            }

            return AuthenticationResponse
                    .builder()
                    .user(null)
                    .message(ErrorMessages.TOKEN_IS_EXPIRED)
                    .code(ReturnCodes.TOKEN_IS_EXPIRED)
                    .successful(false)
                    .build();

        } catch (Exception e) {
            return AuthenticationResponse
                    .builder()
                    .user(null)
                    .message(e.getMessage())
                    .code(ReturnCodes.SOMETHING_IS_WRONG)
                    .successful(false)
                    .build();
        }
    }
}
