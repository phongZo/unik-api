package com.lv.api.dto.store;

import lombok.Data;

@Data
public class VerifyStoreDto {
    private Long id;
    private String token;
    private String name;
    private Boolean isAcceptOrder;
}
