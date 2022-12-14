package com.unik.api.dto.employee;

import com.unik.api.dto.account.AccountDto;
import com.unik.api.dto.category.CategoryDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDto {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("account")
    private AccountDto account;

    @ApiModelProperty("department")
    private CategoryDto department;

    @ApiModelProperty("job")
    private CategoryDto job;

    @ApiModelProperty("note")
    private String note;
}
