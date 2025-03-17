package com.sglab.SGLabDeskApi.email;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IMailRepository extends CrudRepository<MailEntity, UUID> {
}