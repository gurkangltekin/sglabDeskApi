package com.sglab.SGLabDeskApi.contact.responses;

import com.sglab.SGLabDeskApi.contact.ContactEntity;
import com.sglab.SGLabDeskApi.users.UsersEntity;
import com.sglab.SGLabDeskApi.utils.ResponseBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ContactsResponse extends ResponseBase implements Serializable {
    List<ContactEntity> data;
}
