package com.lv.api.dto.rank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankDto {
    private Long id;
    private String name;
    private String avatar;
    private Long target;
}
