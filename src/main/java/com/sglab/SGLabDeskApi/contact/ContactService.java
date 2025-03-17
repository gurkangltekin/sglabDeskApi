package com.sglab.SGLabDeskApi.contact;

import com.sglab.SGLabDeskApi.auth.AuthenticationResponse;
import com.sglab.SGLabDeskApi.contact.responses.ContactsResponse;
import com.sglab.SGLabDeskApi.users.IUsersRepository;
import com.sglab.SGLabDeskApi.users.UsersEntity;
import com.sglab.SGLabDeskApi.utils.ErrorKeys;
import com.sglab.SGLabDeskApi.utils.ErrorMessages;
import com.sglab.SGLabDeskApi.utils.ReturnCodes;
import com.sglab.SGLabDeskApi.utils.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final IUsersRepository userRepository;

    public ServiceResult<ContactsResponse> getContacts(String userId) {

        try{
            List<UsersEntity> userList = userRepository.findAll();



            if(!userList.isEmpty()){

                List<ContactEntity> contactList = new ArrayList<>();

                for(UsersEntity user: userList){
                    contactList.add(new ContactEntity(user.getId().toString(), user.getId().toString(), user.getName(), user.getDescription(), user.getAvatar(), false));
                }

                return ServiceResult
                        .<ContactsResponse>builder()
                        .data(
                                ContactsResponse.builder()
                                        .code(ReturnCodes.GET_CONTACTS_SUCCESFULL)
                                        .data(contactList)
                                        .successful(true)
                                        .build()
                        )
                        .success(true)
                        .code(ReturnCodes.GET_CONTACTS_SUCCESFULL)
                        .build();
            }
            else {
                return ServiceResult
                        .<ContactsResponse>builder()
                        .data(
                                ContactsResponse
                                        .builder()
                                        .data(null)
                                        .code(ReturnCodes.CONTACTS_CANNOT_FIND)
                                        .message(ErrorMessages.CONTACTS_CANNOT_FIND)
                                        .successful(false)
                                        .build())
                        .success(false)
                        .code(ReturnCodes.CONTACTS_CANNOT_FIND)
                        .errorKey(ErrorKeys.CONTACTS_CANNOT_FIND)
                        .build();
            }
        }
        catch (Exception ex){
            return ServiceResult
                .<ContactsResponse>builder()
                .data(
                        ContactsResponse
                                .builder()
                                .data(null)
                                .code(ReturnCodes.SOMETHING_IS_WRONG)
                                .message(ex.getMessage())
                                .successful(false)
                                .build())
                    .success(false)
                    .code(ReturnCodes.SOMETHING_IS_WRONG)
                    .build();
        }
    }
}
