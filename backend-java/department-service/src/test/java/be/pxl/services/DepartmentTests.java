package be.pxl.services;

import be.pxl.services.domain.Department;
import be.pxl.services.repository.DepartmentRepository;
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

@SpringBootTest(classes = be.pxl.services.DepartmentServiceApplication.class)
@AutoConfigureMockMvc
@Testcontainers
public class DepartmentTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartmentRepository departmentRepository;

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
        departmentRepository.deleteAll();
    }

    @Test
    void testCreateDepartment() throws Exception {
        String newDepartmentJson = """
            {
                "name": "IT",
                "organizationId": 1
            }
        """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newDepartmentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("IT"));
    }

    @Test
    void testGetAllDepartments() throws Exception {
        departmentRepository.save(Department.builder()
                .name("Finance").organizationId(1L).build());
        departmentRepository.save(Department.builder()
                .name("Marketing").organizationId(1L).build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/department"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Finance")));
    }

    @Test
    void testFindDepartmentById() throws Exception {
        Department department = departmentRepository.save(Department.builder()
                .name("HR").organizationId(2L).build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/department/" + department.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("HR"))
                .andExpect(jsonPath("$.organizationId").value(2L));
    }

    @Test
    void testFindDepartmentsByOrganization() throws Exception {
        departmentRepository.save(Department.builder()
                .name("Sales").organizationId(10L).build());
        departmentRepository.save(Department.builder()
                .name("Support").organizationId(20L).build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/department/organization/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Sales"));
    }
}
