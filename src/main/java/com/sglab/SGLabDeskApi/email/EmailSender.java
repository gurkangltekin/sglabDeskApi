package com.sglab.SGLabDeskApi.email;

public interface EmailSender {
    void send(String to, String email, String subject);
}
