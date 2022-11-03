package com.lv.api.dto.customer;

import com.lv.api.dto.account.AccountDto;
import com.lv.api.dto.rank.RankDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("account")
    private AccountDto account;

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
