package be.pxl.services.service;

import be.pxl.services.domain.Organization;
import be.pxl.services.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService implements IOrganizationService{

    private OrganizationRepository organizationRepository;

    public Organization findById(Long id) {
        return organizationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Organization> findByIdWithDepartment(Long id) {
        return organizationRepository.findByIdWithDepartment(id);
    }

    @Override
    public List<Organization> findByIdWithDepartmentsAndEmployees(Long id) {
        return organizationRepository.findByIdWithDepartmentsAndEmployees(id);
    }

    @Override
    public List<Organization> findByIdWithEmployees(Long id) {
        return organizationRepository.findByIdWithEmployees(id);
    }
}
