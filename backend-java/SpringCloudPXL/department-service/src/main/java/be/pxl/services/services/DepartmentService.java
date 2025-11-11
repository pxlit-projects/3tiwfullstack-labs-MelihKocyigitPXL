package be.pxl.services.services;

import be.pxl.services.domain.Department;
import be.pxl.services.domain.Employee;
import be.pxl.services.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService implements IDepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department add(Department department) {
        return departmentRepository.save(department);
    }
    public Department findById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public List<Department> findByOrganization(Long organizationId) {
        return departmentRepository.findByOrganizationId(organizationId);
    }

//    public List<Department> findByOrganizationWithEmployees(Long organizationId) {
//        return departmentRepository.findByOrganizationIdWithEmployees(organizationId);
//    }

}
