package com.sglab.SGLabDeskApi.utils;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEntity {

    @Setter
    @Getter
    @Column(name = "id")
    @GeneratedValue
    @jakarta.persistence.Id
    UUID id;

    @Column(name = "is_deleted")
    boolean isDeleted;

    @Setter
    @Getter
    @Column(name = "version")
    LocalDateTime version;

    @Setter
    @Getter
    @Column(name = "create_date")
    LocalDateTime createdDate;

    public BaseEntity(UUID id, boolean isDeleted, LocalDateTime version, LocalDateTime createdDate) {
        this.id = id;
        this.isDeleted = isDeleted;
        this.version = version;
        this.createdDate = createdDate;
    }

    public BaseEntity(boolean isDeleted, LocalDateTime version, LocalDateTime createdDate) {
        this.isDeleted = isDeleted;
        this.version = version;
        this.createdDate = createdDate;
    }

    public void setIsDeleted(boolean isDeleted){
        this.isDeleted = isDeleted;
    }

}