package com.example.PRISM.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MachineDto {
    private Long id;
    private String name;
    private String type;
    private String code;
    private String status;
    private String location;

}
