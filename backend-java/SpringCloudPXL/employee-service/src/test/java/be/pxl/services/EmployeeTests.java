package be.pxl.services;

import be.pxl.services.domain.Employee;
import be.pxl.services.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = be.pxl.services.EmployeeServiceApplication.class)
@AutoConfigureMockMvc
@Testcontainers
public class EmployeeTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Container
    private static MySQLContainer<?> sqlContainer =
            new MySQLContainer<>("mysql:5.7.37");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlContainer::getUsername);
        registry.add("spring.datasource.password", sqlContainer::getPassword);
    }

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
    }

    @Test
    void testCreateEmployee() throws Exception {
        String newEmployeeJson = """
            {
                "organizationId": 1,
                "departmentId": 2,
                "name": "John Doe",
                "age": 30,
                "position": "Developer"
            }
        """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEmployeeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.position").value("Developer"));
    }

    @Test
    void testGetAllEmployees() throws Exception {
        employeeRepository.save(Employee.builder()
                .name("Alice Smith").age(25).position("Analyst")
                .organizationId(1L).departmentId(10L).build());
        employeeRepository.save(Employee.builder()
                .name("Bob Johnson").age(40).position("Manager")
                .organizationId(1L).departmentId(20L).build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Alice Smith")));
    }

    @Test
    void testFindEmployeeById() throws Exception {
        Employee employee = employeeRepository.save(Employee.builder()
                .name("Carol Brown").age(28).position("HR")
                .organizationId(2L).departmentId(3L).build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/" + employee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Carol Brown"))
                .andExpect(jsonPath("$.position").value("HR"));
    }

    @Test
    void testFindEmployeesByDepartment() throws Exception {
        employeeRepository.save(Employee.builder()
                .name("Dan White").age(32).position("Technician")
                .organizationId(1L).departmentId(10L).build());
        employeeRepository.save(Employee.builder()
                .name("Eve Black").age(29).position("Designer")
                .organizationId(1L).departmentId(20L).build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/department/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Dan White"));
    }

    @Test
    void testFindEmployeesByOrganization() throws Exception {
        employeeRepository.save(Employee.builder()
                .name("Frank Stone").age(35).position("Architect")
                .organizationId(100L).departmentId(1L).build());
        employeeRepository.save(Employee.builder()
                .name("Grace Lee").age(27).position("Tester")
                .organizationId(200L).departmentId(1L).build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/organization/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Frank Stone"));
    }
}
