package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.*;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import com.sky.vo.PageVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.sky.constant.JwtClaimsConstant.TOKEN_VERSION;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        claims.put(TOKEN_VERSION, employee.getTokenVersion());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 修改密码
     *
     * @param passwordEditDTO
     * @return
     */
    @PutMapping("/editPassword")
    @ApiOperation("修改密码")
    public Result<String> editPassword(@Valid @RequestBody PasswordEditDTO passwordEditDTO) {
        return employeeService.editPassword(passwordEditDTO);
    }

    /*
    * 分页查询
    **/
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageVO<Employee>> page(EmployeePageQueryDTO employeePageQueryDTO) {
        PageVO<Employee> pageVO = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageVO);
    }

    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.save(employeeDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("修改账户状态")
    public Result updateStatus(@PathVariable Integer status ,Long id) {
        employeeService.updateStatus(status, id);
        return Result.success();
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工")
    public Result<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        if (employee == null) {
            return Result.error("员工不存在");
        }
        return Result.success(employee);
    }

    /**
     * 根据id修改员工信息
     * @param employeeDTO
     * @return
     */
    @PutMapping()
    @ApiOperation("修改员工信息")
    public Result update(@Valid @RequestBody EmployeeDTO employeeDTO) {
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
