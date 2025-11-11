package be.pxl.services.services;

import be.pxl.services.domain.Department;

import java.util.List;

public interface IDepartmentService {

    List<Department> getAllDepartments();
    Department findById(Long id);
    Department add(Department department);
    List<Department> findByOrganization(Long organizationId);
//    List<Department> findByOrganizationWithEmployees(Long organizationId);
}
