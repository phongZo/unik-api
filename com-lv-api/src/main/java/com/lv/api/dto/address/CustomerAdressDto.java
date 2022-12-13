package com.lv.api.dto.address;

import com.lv.api.dto.ABasicAdminDto;
import com.lv.api.dto.customer.CustomerDto;
import lombok.Data;

@Data
public class CustomerAdressDto extends ABasicAdminDto {
    private CustomerDto customerDto;
    private String addressDetails;
    private String receiverFullName;
    private String phone;
    private Boolean isDefault;
    private String note;
}
