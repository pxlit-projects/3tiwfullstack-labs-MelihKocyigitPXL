package be.pxl.services.services;

import be.pxl.services.domain.Employee;

import java.util.List;

public interface IEmployeeService {

    List<Employee> getAllEmployees();
    Employee add(Employee employee);
    Employee findById(Long id);
    List<Employee> findByDepartment(Long departmentId);
    List<Employee> findByOrganization(Long organizationId);
}
