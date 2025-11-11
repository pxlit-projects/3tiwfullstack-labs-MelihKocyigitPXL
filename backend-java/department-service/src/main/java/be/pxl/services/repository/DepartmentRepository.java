package be.pxl.services.repository;

import be.pxl.services.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByOrganizationId(Long organizationId);
    @Query("SELECT d from Department d LEFT JOIN FETCH d.employees WHERE d.organizationId = :organization_id")
    List<Department> findByOrganizationIdWithEmployees(Long organizationId);

}
