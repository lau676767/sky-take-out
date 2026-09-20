package com.sky.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class EmployeeDTO implements Serializable {

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private String sex;

    @NotBlank
    private String idNumber;

    private LocalDateTime updateTime;

    private Long updateUser;

}
