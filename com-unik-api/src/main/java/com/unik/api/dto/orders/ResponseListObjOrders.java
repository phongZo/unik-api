package com.unik.api.dto.orders;

import com.unik.api.dto.ResponseListObj;
import lombok.Data;

@Data
public class ResponseListObjOrders extends ResponseListObj<OrdersDto> {
    private Double sumMoney;
}
