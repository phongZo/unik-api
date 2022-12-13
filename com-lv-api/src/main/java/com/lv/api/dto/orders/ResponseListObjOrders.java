package com.lv.api.dto.orders;

import com.lv.api.dto.ResponseListObj;
import lombok.Data;

@Data
public class ResponseListObjOrders extends ResponseListObj<OrdersDto> {
    private Double sumMoney;
}
