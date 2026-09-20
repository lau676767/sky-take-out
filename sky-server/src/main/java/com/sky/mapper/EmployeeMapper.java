package com.sky.mapper;

import com.sky.dto.EmployeeDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Select("select * from employee where id = #{empId}")
    Employee getById(Long empId);

    @Update("update employee set password = #{newPassword} where id = #{empId}")
    int editPassword(Long empId, String newPassword);

    List<Employee> pageQuery(String name);

    @Insert("insert into employee (id, username, name, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "values (#{id}, #{username}, #{name}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Employee employee);

    void updateStatus(Employee employee);

    @Update("update employee set token_version = token_version + 1 where id = #{empId}")
    void increaseTokenVersion(Long empId);

    @Update("update employee set name = #{name},username= #{username}, phone = #{phone}, sex = #{sex}, id_number = #{idNumber}, update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    void update(EmployeeDTO employeeDTO);
}
