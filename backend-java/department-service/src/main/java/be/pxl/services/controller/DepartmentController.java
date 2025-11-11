package be.pxl.services.controller;

import be.pxl.services.domain.Department;
import be.pxl.services.domain.Employee;
import be.pxl.services.services.IDepartmentService;
import be.pxl.services.services.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final IDepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<Department>> getDepartments() {
        return new ResponseEntity<>(departmentService.getAllDepartments(), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<Department> addDepartment(@RequestBody Department department) {
        return new ResponseEntity<>(departmentService.add(department), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> findById(@PathVariable Long id) {
        return new ResponseEntity<>(departmentService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Department>> findByOrganization(@PathVariable Long organizationId) {
        return new ResponseEntity<>(departmentService.findByOrganization(organizationId), HttpStatus.OK);
    }
}
