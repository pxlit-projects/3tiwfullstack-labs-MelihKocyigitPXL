package be.pxl.services.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="organization")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String address;
    @Transient
    private List<Employee> employees;
    @Transient
    private List<Department> departments;
}
