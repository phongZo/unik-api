package com.unik.api.dto.address;

import com.unik.api.dto.ABasicAdminDto;
import com.unik.api.dto.customer.CustomerDto;
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
