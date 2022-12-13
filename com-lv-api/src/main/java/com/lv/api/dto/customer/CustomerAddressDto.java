package com.lv.api.dto.customer;

import com.lv.api.dto.ABasicAdminDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerAddressDto extends ABasicAdminDto {
    private Long id;
    private CustomerDto customerDto;
    private String addressDetails;
    private String receiverFullName;
    private String phone;
    private Boolean isDefault;
    private String note;
    private Integer typeAddress;
}
