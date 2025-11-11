package be.pxl.services;

import be.pxl.services.domain.Notification;
import be.pxl.services.repository.NotificationRepository;
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

@SpringBootTest(classes = be.pxl.services.NotificationServiceApplication.class)
@AutoConfigureMockMvc
@Testcontainers
public class NotificationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationRepository notificationRepository;

    @Container
    private static final MySQLContainer<?> sqlContainer =
            new MySQLContainer<>("mysql:5.7.37");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlContainer::getUsername);
        registry.add("spring.datasource.password", sqlContainer::getPassword);
    }

    @BeforeEach
    void setup() {
        notificationRepository.deleteAll();
    }

    @Test
    void testCreateNotification() throws Exception {
        String newNotificationJson = """
            {
                "from": "admin@pxl.be",
                "to": "user@pxl.be",
                "subject": "System Update",
                "message": "Maintenance tonight at 22:00."
            }
        """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newNotificationJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.from").value("admin@pxl.be"))
                .andExpect(jsonPath("$.subject").value("System Update"));
    }

    @Test
    void testGetAllNotifications() throws Exception {
        notificationRepository.save(Notification.builder()
                .from("john@pxl.be").to("mary@pxl.be")
                .subject("Reminder").message("Meeting at 10:00.").build());
        notificationRepository.save(Notification.builder()
                .from("hr@pxl.be").to("staff@pxl.be")
                .subject("Policy Update").message("New guidelines.").build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/notification"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].subject", is("Reminder")));
    }

    @Test
    void testGetNotificationById() throws Exception {
        Notification notification = notificationRepository.save(Notification.builder()
                .from("info@pxl.be").to("user@pxl.be")
                .subject("Welcome").message("Your account is ready.").build());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/notification/" + notification.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject").value("Welcome"))
                .andExpect(jsonPath("$.message").value("Your account is ready."));
    }

    @Test
    void testUpdateNotification() throws Exception {
        Notification notification = notificationRepository.save(Notification.builder()
                .from("system@pxl.be").to("alex@pxl.be")
                .subject("Old Subject").message("Old message").build());

        String updateJson = """
            {
                "from": "system@pxl.be",
                "to": "alex@pxl.be",
                "subject": "New Subject",
                "message": "Updated message content"
            }
        """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/notification/" + notification.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject").value("New Subject"))
                .andExpect(jsonPath("$.message").value("Updated message content"));
    }

    @Test
    void testDeleteNotification() throws Exception {
        Notification notification = notificationRepository.save(Notification.builder()
                .from("delete@pxl.be").to("target@pxl.be")
                .subject("Delete Me").message("This should be deleted").build());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/notification/" + notification.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/notification"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
