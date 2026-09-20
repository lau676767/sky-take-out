package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.sky.constant.PasswordConstant.DEFAULT_PASSWORD;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //密码比对
        //进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override
    public Result<String> editPassword(PasswordEditDTO passwordEditDTO) {
        Long empId = BaseContext.getCurrentId();
        String oldPassword = passwordEditDTO.getOldPassword();
        String newPassword = passwordEditDTO.getNewPassword();
        // 参数校验
        if (oldPassword == null || oldPassword.isBlank()) {
            return Result.error("原密码不能为空");
        }
        if (newPassword == null || newPassword.isBlank()) {
            return Result.error("新密码不能为空");
        }
        if (oldPassword.equals(newPassword)) {
            return Result.error("新密码不能与原密码相同");
        }
        // 简单密码长度校验，按需调整
        if(newPassword.length() < 6){
            return Result.error("新密码长度不能少于6位");
        }
        if (!newPassword.matches("^[A-Za-z0-9]{6,20}$")) {
            return Result.error("新密码必须6-20位，只能是数字或字母，区分大小写");
        }
        Employee employee = employeeMapper.getById(empId);
        //判断用户是否存在
        if (employee == null) {
            return Result.error("用户不存在");
        }
        //判断原密码是否正确
        oldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if (!oldPassword.equals(employee.getPassword())) {
            return Result.error("原密码不正确");
        }
        //原密码正确
        newPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        employeeMapper.editPassword(empId, newPassword);
        employeeMapper.increaseTokenVersion(empId);
        return Result.success("修改成功");
    }

    @Override
    public PageVO<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //获取分页查询参数
        int page = employeePageQueryDTO.getPage();
        int pageSize = employeePageQueryDTO.getPageSize();
        String name = employeePageQueryDTO.getName();
        //分页查询
        PageHelper.startPage(page, pageSize);
        List<Employee> employees = employeeMapper.pageQuery(name);
        //获取分页总记录数total
        Page<Employee> pageInfo = (Page<Employee>) employees;
        long total = pageInfo.getTotal();
        List<Employee> records = pageInfo.getResult();
        return new PageVO<>(total, records);
    }

    @Override
    public void save(EmployeeDTO employeeDTO) {
        //校验手机号格式
        if (!employeeDTO.getPhone().matches("^[1]([3-9])[0-9]{9}$")) {
            throw new IllegalArgumentException("手机格式不正确");
        }
        //将性别限制0和1
        if (Integer.parseInt(employeeDTO.getSex()) != 0 && Integer.parseInt(employeeDTO.getSex()) != 1) {
            throw new IllegalArgumentException("性别必须是0或1");
        }
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(DEFAULT_PASSWORD.getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.insert(employee);
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        Long empId = BaseContext.getCurrentId();
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.updateStatus(employee);
    }

    @Override
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }

    @Override
    public void update(EmployeeDTO employeeDTO) {
        //将性别限制0和1
        if (Integer.parseInt(employeeDTO.getSex()) != 0 && Integer.parseInt(employeeDTO.getSex()) != 1) {
            throw new IllegalArgumentException("性别必须是0或1");
        }
        //校验手机格式
        if (!employeeDTO.getPhone().matches("^[1]([3-9])[0-9]{9}$")) {
            throw new IllegalArgumentException("手机格式不正确");
        }
        employeeDTO.setUpdateTime(LocalDateTime.now());
        employeeDTO.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employeeDTO);
    }


}
