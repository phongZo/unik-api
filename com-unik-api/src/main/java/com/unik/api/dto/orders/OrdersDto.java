package com.unik.api.dto.orders;

import com.unik.api.dto.ABasicAdminDto;
import com.unik.api.dto.customer.CustomerAddressDto;
import com.unik.api.dto.customer.CustomerDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrdersDto extends ABasicAdminDto {
    private Long id;
    private Integer amount;
    private CustomerDto customerDto;
    private Integer saleOff;
    private Double totalMoney;
    private Integer vat;
    private Integer state;
    private Integer prevState;
    private String code;
    private Integer paymentMethod;
    private Double deliveryFee;
    private Long storeId;
    private LocalDate expectedReceiveDate;
    private String note;
    private Double saleOffMoney;
    private CustomerAddressDto customerAddressDto;
    private List<OrdersDetailDto> ordersDetailDtoList;
}
