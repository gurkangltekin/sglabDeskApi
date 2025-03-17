package com.sglab.SGLabDeskApi.email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sglab.SGLabDeskApi.utils.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "mails")
public class MailEntity extends BaseEntity {

    @Column(name = "body")
    @JsonIgnore
    private String body;

    @Column(name = "subject")
    @JsonIgnore
    private String subject;

    @Column(name = "mail_address")
    @JsonIgnore
    private String mailAddress;

    @Column(name = "is_send")
    @JsonIgnore
    private Boolean isSend;

    @Column(name = "send_date")
    @JsonIgnore
    private LocalDateTime sendDate;

    @Column(name = "detail")
    @JsonIgnore
    private String detail;

    public MailEntity(UUID id, boolean isDeleted, LocalDateTime version, LocalDateTime createdDate, String subject, String body, String mailAddress, Boolean isSend, LocalDateTime sendDate) {
        super(id, isDeleted, version, createdDate);
        this.body = body;
        this.subject = subject;
        this.mailAddress = mailAddress;
        this.isSend = isSend;
        this.sendDate = sendDate;
    }

    public MailEntity(UUID id, boolean isDeleted, LocalDateTime version, LocalDateTime createdDate, String subject, String body, String mailAddress, Boolean isSend, LocalDateTime sendDate,String detail) {
        super(id, isDeleted, version, createdDate);
        this.body = body;
        this.subject = subject;
        this.mailAddress = mailAddress;
        this.isSend = isSend;
        this.sendDate = sendDate;
        this.detail = detail;
    }
}
