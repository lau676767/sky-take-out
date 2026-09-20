package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.Employee;
import com.sky.result.Result;
import com.sky.vo.PageVO;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    Result<String> editPassword(PasswordEditDTO passwordEditDTO);

    PageVO<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void save(EmployeeDTO employeeDTO);

    void updateStatus(Integer status, Long id);

    Employee getById(Long id);

    void update(EmployeeDTO employeeDTO);
}
