package be.pxl.services.controller;

import be.pxl.services.EmployeeServiceApplication;
import be.pxl.services.domain.Employee;
import be.pxl.services.services.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final IEmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> getEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @PostMapping ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.add(employee), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> findById(@PathVariable Long id) {
        return new ResponseEntity<>(employeeService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Employee>> findByDepartment(@PathVariable Long departmentId) {
        return new ResponseEntity<>(employeeService.findByDepartment(departmentId), HttpStatus.OK);
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Employee>> findByOrganization(@PathVariable   Long organizationId) {
        return new ResponseEntity<>(employeeService.findByOrganization(organizationId), HttpStatus.OK);
    }
}
