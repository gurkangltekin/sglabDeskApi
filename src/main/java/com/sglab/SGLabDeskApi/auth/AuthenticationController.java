package com.sglab.SGLabDeskApi.auth;

import com.sglab.SGLabDeskApi.utils.ReturnCodes;
import com.sglab.SGLabDeskApi.utils.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        try{
            ServiceResult<AuthenticationResponse> serviceResult = authService.register(request);

            if(serviceResult.getSuccess()){
                return ResponseEntity.ok(serviceResult.getData());
            }
            else {
                return ResponseEntity.ok(AuthenticationResponse
                        .builder()
                        .message(serviceResult.getErrorKey())
                        .successful(false)
                        .code(ReturnCodes.SOMETHING_IS_WRONG)
                        .build());
            }
        }
        catch (Exception ex){
            return ResponseEntity.ok(AuthenticationResponse
                    .builder()
                    .message(ex.getMessage())
                    .successful(false)
                    .code(ReturnCodes.SOMETHING_IS_WRONG)
                    .build());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
    ){
        try{
            return ResponseEntity.ok(authService.authenticate(request));
        }
        catch (Exception ex){
            return ResponseEntity.ok(AuthenticationResponse
                    .builder()
                    .message(ex.getMessage())
                    .successful(false)
                    .code(ReturnCodes.SOMETHING_IS_WRONG)
                    .build());
        }
    }

    @PostMapping("/loginWithThirdParts")
    public ResponseEntity<AuthenticationResponse> loginWithThirdParts(
            @RequestBody AuthenticationRequest request
    ){
        try{
            return ResponseEntity.ok(authService.authenticateWithThirdParts(request));
        }
        catch (Exception ex){
            return ResponseEntity.ok(AuthenticationResponse
                    .builder()
                    .message(ex.getMessage())
                    .successful(false)
                    .code(ReturnCodes.SOMETHING_IS_WRONG)
                    .build());
        }
    }

    @PostMapping("/controlTokenIsExpired")
    public ResponseEntity<AuthenticationResponse> controlTokenIsExpired(@RequestParam String token){
        try{
            return ResponseEntity.ok(authService.controlTokenIsExpired(token));
        }
        catch (Exception ex){
            return ResponseEntity.ok(AuthenticationResponse
                    .builder()
                    .message(ex.getMessage())
                    .successful(false)
                    .code(ReturnCodes.SOMETHING_IS_WRONG)
                    .build());
        }
    }
}
