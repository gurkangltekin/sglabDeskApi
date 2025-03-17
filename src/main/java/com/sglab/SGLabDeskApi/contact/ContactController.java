package com.sglab.SGLabDeskApi.contact;

import com.sglab.SGLabDeskApi.auth.AuthenticationResponse;
import com.sglab.SGLabDeskApi.contact.responses.ContactsResponse;
import com.sglab.SGLabDeskApi.utils.ReturnCodes;
import com.sglab.SGLabDeskApi.utils.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @CrossOrigin(origins = "*")
    @PostMapping("/getContacts")
    public ResponseEntity<ContactsResponse> getContacts(@RequestParam String userId){
        try{
            ServiceResult<ContactsResponse> serviceResult = contactService.getContacts(userId);

            if(serviceResult.getSuccess()){
                return ResponseEntity.ok(serviceResult.getData());
            }
            else {
                return ResponseEntity.ok(ContactsResponse
                        .builder()
                        .message(serviceResult.getErrorKey())
                        .successful(false)
                        .code(ReturnCodes.SOMETHING_IS_WRONG)
                        .build());
            }
        } catch (Exception ex) {
            return ResponseEntity.ok(ContactsResponse
                    .builder()
                    .message(ex.getMessage())
                    .successful(false)
                    .code(ReturnCodes.SOMETHING_IS_WRONG)
                    .build());
        }
    }
}
