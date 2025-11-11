package be.pxl.services.controller;

import be.pxl.services.domain.Organization;
import be.pxl.services.service.IOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final IOrganizationService organizationService;

    @GetMapping("{id}")
    public ResponseEntity<Organization> findById(@PathVariable Long id) {
        return new ResponseEntity<>(organizationService.findById(id), HttpStatus.OK);
    }

    @GetMapping("{id}/with-departments")
    public ResponseEntity<List<Organization>> findByIdWithDepartments(@PathVariable Long id) {
        return new ResponseEntity<>(organizationService.findByIdWithDepartment(id), HttpStatus.OK);
    }

    @GetMapping("{id}/with-departments-and-employees")
    public ResponseEntity<List<Organization>> findByIdWithDepartmentsAndEmployees(@PathVariable Long id) {
        return new ResponseEntity<>(organizationService.findByIdWithDepartmentsAndEmployees(id), HttpStatus.OK);
    }

    @GetMapping("{id}/with-employees")
    public ResponseEntity<List<Organization>> findByIdWithEmployees(@PathVariable Long id) {
        return new ResponseEntity<>(organizationService.findByIdWithEmployees(id), HttpStatus.OK);
    }


}
