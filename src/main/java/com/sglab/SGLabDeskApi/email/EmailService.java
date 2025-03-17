package com.sglab.SGLabDeskApi.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{
    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);

    private final IMailRepository repo;
    private final JavaMailSender mailSender;
    @Override
    @Async
    public void send(String to, String email, String subject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");



            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("info@sglab.dev");
            mailSender.send(mimeMessage);

            repo.save(new MailEntity(
                    UUID.randomUUID(),
                    false,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    subject,
                    email,
                    to,
                    true,
                    LocalDateTime.now()
            ));

        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);

            repo.save(new MailEntity(
                    UUID.randomUUID(),
                    false,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    subject,
                    email,
                    to,
                    false,
                    LocalDateTime.now(),
                    e.getMessage()
            ));
            throw new IllegalStateException("failed to send email");
        }
    }
}
