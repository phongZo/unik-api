package com.lv.api.dto.customer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerAddressDto {
    private Long id;
    private String province;
    private String district;
    private String ward;
    private String addressDetails;
    private String receiverFullName;
    private String phone;
    private Boolean isDefault;
    private String note;
}
