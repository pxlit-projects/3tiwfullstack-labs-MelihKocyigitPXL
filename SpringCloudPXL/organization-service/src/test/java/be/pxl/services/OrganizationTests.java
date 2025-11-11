package be.pxl.services;

import be.pxl.services.domain.Organization;
import be.pxl.services.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = be.pxl.services.OrganizationServiceApplication.class)
@AutoConfigureMockMvc
@Testcontainers
public class OrganizationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Container
    private static MySQLContainer<?> sqlContainer =
            new MySQLContainer<>("mysql:5.7.37");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlContainer::getUsername);
        registry.add("spring.datasource.password", sqlContainer::getPassword);
    }

    @MockitoBean
    private EmployeeServiceApplication employeeServiceApplication;

    @BeforeEach
    void setup() {
        organizationRepository.deleteAll();
        Mockito.when(employeeServiceApplication.getz)
    }

    @Test
    void testCreateAndFindOrganizationById() throws Exception {
        Organization org = organizationRepository.save(Organization.builder()
                .name("Tech Corp").build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/organization/" + org.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tech Corp"));
    }

    @Test
    void testFindOrganizationWithDepartments() throws Exception {
        Organization org = organizationRepository.save(Organization.builder()
                .name("Business Inc").build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/organization/" + org.getId() + "/with-departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(org.getId()))
                .andExpect(jsonPath("$[0].name").value("Business Inc"));
    }

    @Test
    void testFindOrganizationWithDepartmentsAndEmployees() throws Exception {
        Organization org = organizationRepository.save(Organization.builder()
                .name("Global Co").build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/organization/" + org.getId() + "/with-departments-and-employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(org.getId()))
                .andExpect(jsonPath("$[0].name").value("Global Co"));
    }

    @Test
    void testFindOrganizationWithEmployees() throws Exception {
        Organization org = organizationRepository.save(Organization.builder()
                .name("Enterprise Ltd").build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/organization/" + org.getId() + "/with-employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(org.getId()))
                .andExpect(jsonPath("$[0].name").value("Enterprise Ltd"));
    }
}
