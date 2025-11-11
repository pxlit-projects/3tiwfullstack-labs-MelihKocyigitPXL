package be.pxl.services.repository;

import be.pxl.services.domain.Department;
import be.pxl.services.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    @Query("SELECT o from Organization o LEFT JOIN FETCH o.departments WHERE o.id = :id")
    List<Organization> findByIdWithDepartment(Long id);

    @Query("SELECT DISTINCT o FROM Organization o " +
            "LEFT JOIN FETCH o.departments d " +
            "LEFT JOIN FETCH d.employees " +
            "WHERE o.id = :id")
    List<Organization> findByIdWithDepartmentsAndEmployees(Long id);

    @Query("SELECT o FROM Organization o LEFT JOIN FETCH o.employees WHERE o.id = :id")
    List<Organization> findByIdWithEmployees(Long id);
}
