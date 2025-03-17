package com.sglab.SGLabDeskApi.contact;

import com.sglab.SGLabDeskApi.utils.BaseEntity;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContactEntity extends BaseEntity {
    private String baseUserId;
    private String contactUserId;
    private String name;
    private String description;
    private String avatar;
    private boolean isOnline;
}
