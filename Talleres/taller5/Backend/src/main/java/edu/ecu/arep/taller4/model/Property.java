package edu.ecu.arep.taller4.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "properties")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 255)
    private String address;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private int size;
    @Column(columnDefinition = "TEXT")
    private String description;
}
