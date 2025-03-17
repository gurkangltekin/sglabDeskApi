package com.sglab.SGLabDeskApi.users;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUsersRepository extends JpaRepository<UsersEntity, UUID> {

    Optional<UsersEntity> findByUsername(String username);
    Optional<UsersEntity> findByEmail(String email);

    @Override
    Optional<UsersEntity> findById(UUID id);
    Optional<UsersEntity> findByIdAndIsDeletedIsFalse(UUID id);

//    @Query("SELECT a FROM UserEntity a  WHERE a.username = ?1")
    Optional<UsersEntity> getEmailByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE UsersEntity a " +
            "SET a.isEnabled = TRUE WHERE a.email = ?1")
    int enableUser(String email);

//    @Transactional
//    @Modifying
//    @Query("UPDATE UserEntity a SET a.version = NOW(), a.isDeleted = TRUE WHERE a.id = ?1")
//    int deleteUser(@Param("id") UUID id);
}
