package be.pxl.services.service;

import be.pxl.services.domain.Organization;

import java.util.List;

public interface IOrganizationService {
    Organization findById(Long id);
    List<Organization> findByIdWithDepartment(Long id);
    List<Organization> findByIdWithDepartmentsAndEmployees(Long id);
    List<Organization> findByIdWithEmployees(Long id);
}
