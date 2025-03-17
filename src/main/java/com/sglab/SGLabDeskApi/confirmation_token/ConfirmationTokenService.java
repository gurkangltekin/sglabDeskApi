package com.sglab.SGLabDeskApi.confirmation_token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final IConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setExpiresAt(String token) {
        return confirmationTokenRepository.setExpiresAt(
                token, LocalDateTime.now());
    }

    public int setExpiresAt(UUID id) {
        return confirmationTokenRepository.setExpiresAt(
                id, LocalDateTime.now());
    }
}