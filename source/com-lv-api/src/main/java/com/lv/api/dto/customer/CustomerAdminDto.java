package com.lv.api.dto.customer;

import com.lv.api.dto.account.AccountAdminDto;
import com.lv.api.dto.rank.RankDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CustomerAdminDto {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("account")
    private AccountAdminDto account;

    @ApiModelProperty("gender")
    private Integer gender;

    @ApiModelProperty("birthday")
    private LocalDate birthday;

    @ApiModelProperty("rank")
    private RankDto rank;

    @ApiModelProperty("note")
    private String note;

    @ApiModelProperty("addresses")
    private List<CustomerAddressDto> addresses;
}
