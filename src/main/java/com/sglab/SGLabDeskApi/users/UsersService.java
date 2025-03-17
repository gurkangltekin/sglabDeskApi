package com.sglab.SGLabDeskApi.users;

import com.sglab.SGLabDeskApi.auth.RegisterRequest;
import com.sglab.SGLabDeskApi.confirmation_token.ConfirmationToken;
import com.sglab.SGLabDeskApi.confirmation_token.ConfirmationTokenService;
import com.sglab.SGLabDeskApi.confirmation_token.IConfirmationTokenRepository;
import com.sglab.SGLabDeskApi.email.EmailSender;
import com.sglab.SGLabDeskApi.utils.ErrorKeys;
import com.sglab.SGLabDeskApi.utils.ServiceResult;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@AllArgsConstructor
public class UsersService implements UserDetailsService {
    private final IUsersRepository repo;
    private final IConfirmationTokenRepository confirmationTokenRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";

    // Get Methods
    public Optional<UsersEntity> getUserByEmail(String email){
        return repo.findByEmail(email);
    }

    public Optional<UsersEntity> getEmailByUsername(String username){
        return repo.getEmailByUsername(username);
    }

    // Create Methods

    public String addUser(UsersEntity user) {
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already taken!");
        } else {
            if (repo.findByUsername(user.getUsername()).isPresent()) {
                throw new IllegalStateException("Username already taken!");
            } else {
                String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                user.setCreatedDate(LocalDateTime.now());
                user.setVersion(LocalDateTime.now());
                repo.save(user);
                String token = UUID.randomUUID().toString();
                ConfirmationToken confirmationToken = new ConfirmationToken(
                        token,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(15),
                        user
                );
                confirmationTokenService.saveConfirmationToken(confirmationToken);
                // TODO: SEND EMAIL
                return token;
            }
        }
    }
//    public ServiceResult<UserEntity> addUserWithThirdPartApplications(UserEntity userEntity){
//        Optional<UserEntity> existingUser = repo.findByEmailOrOpenId(userEntity.getEmail(), userEntity.getOpenId());
//        if(!existingUser.isPresent())
//        {
//            userEntity.setCreatedDate(LocalDateTime.now());
//            userEntity.setVersion(LocalDateTime.now());
//
//            // gelecek zamanda onaylaha ihtiyacı olabilir.
//            userEntity.setEnabled(true);
//
//            userEntity.setId(repo.save(userEntity).getId());
//
//            return new ServiceResult<>(true, userEntity);
//        }
//
//
//        return new ServiceResult<>(true, existingUser.get());
//    }

    // Update Methods

    public int enableUser(String email) {
        return repo.enableUser(email);
    }


    // Delete Methods

    public ServiceResult<Boolean> deleteUser(String userId){
        ServiceResult<Boolean> result = new ServiceResult<>();
        UUID uuid;
        try {
            uuid = UUID.fromString(userId);
        } catch (IllegalArgumentException ex) {
            result.setSuccess(false);
            result.setErrorKey(ErrorKeys.USER_NOT_FOUND);
            return result;
        }
//        int effectedRows = repo.deleteUser(uuid);
//        result.setSuccess(effectedRows != 0);
//        result.setData(true);
        return result;
    }

    // Additional Methods

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String checkUserExist(RegisterRequest req){
        Optional<UsersEntity> userEntity = getUserByEmail(req.getEmail());
        if (userEntity.isPresent()) {
            if(userEntity.get().isEnabled()) return "Bu email zaten kullanimda.";

            Optional<List<ConfirmationToken>> confirmationToken = confirmationTokenRepo.findByUserIdAndConfirmedAtIsNullOrderByCreatedDateDescTopOne(userEntity.get().getId());
            if(confirmationToken.isPresent()){
                ConfirmationToken c = confirmationToken.get().get(0);
                if(c.getExpiresAt().isAfter(LocalDateTime.now())) return "Bu mail için aktif onay maili bulunmaktadır. Daha önce girmiş olduğunuz mail adresine gelen mail üzerinden onaylama işlemini gerçekleştiriniz.";
                sendAgainRegisterMail(userEntity.get());
                return "Bu mail adresi için gönderilen aktifleştirme mailinin süresi dolmuş! Yeni aktivasyon maili gönderildi.";
            }

        }
        userEntity = repo.findByUsername(req.getUsername());
        if (userEntity.isPresent()){
            if(userEntity.get().isEnabled()) return "Bu kullanıcı adı zaten kullanimda.";

            Optional<List<ConfirmationToken>> confirmationToken = confirmationTokenRepo.findByUserIdAndConfirmedAtIsNullOrderByCreatedDateDescTopOne(userEntity.get().getId());
            if(confirmationToken.isPresent()){
                ConfirmationToken c = confirmationToken.get().get(0);
                if(c.getExpiresAt().isAfter(LocalDateTime.now())) return "Bu kullanıcı adı için aktif onay maili bulunmaktadır. Daha önce girmiş olduğunuz '" + userEntity.get().getEmail() +"' mail adresine gelen mail üzerinden onaylama işlemini gerçekleştiriniz.";
                sendAgainRegisterMail(userEntity.get());
                return "Bu kullanıcı adı için gönderilen aktifleştirme mailinin süresi dolmuş! Yeni aktivasyon maili  '" + userEntity.get().getEmail() +"' adresine  gönderildi.";
            }
        }
        return null;
    }

    public void sendAgainRegisterMail(UsersEntity usersEntity){

        String token = UUID.randomUUID().toString();
        ConfirmationToken newConfirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                usersEntity
        );
        confirmationTokenService.saveConfirmationToken(newConfirmationToken);


        String link = "http://localhost:8080/api/users/confirm?token=" + token;

        Context context = new Context();
        context.setVariable("name", usersEntity.getName());
        context.setVariable("link", link);

        String html = templateEngine.process("AgainRegisterTemplate", context);

        emailSender.send(
                usersEntity.getEmail(),
                html,
                "Email Doğrulaması");
    }

}