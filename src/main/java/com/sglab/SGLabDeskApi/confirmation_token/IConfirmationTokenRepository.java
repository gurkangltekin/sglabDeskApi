package com.sglab.SGLabDeskApi.confirmation_token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface IConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {
    Optional<ConfirmationToken> findByToken(String token);

    @Query("SELECT c FROM ConfirmationToken c WHERE c.userEntity.id = ?1")
    Optional<ConfirmationToken> findByUserId(UUID uuid);
    @Query(value = "SELECT c FROM ConfirmationToken c WHERE c.userEntity.id = ?1 AND c.confirmedAt IS NULL ORDER BY c.createdDate DESC")
    Optional<List<ConfirmationToken>> findByUserIdAndConfirmedAtIsNullOrderByCreatedDateDescTopOne(UUID uuid);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c SET c.confirmedAt = ?2 WHERE c.token = ?1")
    int setExpiresAt(String token, LocalDateTime confirmedAt);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c SET c.expiresAt = ?2 WHERE c.userEntity.id = ?1")
    int setExpiresAt(UUID userId, LocalDateTime confirmedAt);
}
