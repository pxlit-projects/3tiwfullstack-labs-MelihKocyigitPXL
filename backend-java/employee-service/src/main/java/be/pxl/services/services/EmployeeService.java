package be.pxl.services.services;

import be.pxl.services.domain.Employee;
import be.pxl.services.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService{

    private final EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee add(Employee employee) {
        return employeeRepository.save(employee);
    }
    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public List<Employee> findByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    public List<Employee> findByOrganization(Long organizationId) {
        return employeeRepository.findByOrganizationId(organizationId);
    }
}
