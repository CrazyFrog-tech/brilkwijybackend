package nl.spring.brilkwijt.dto;


import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;

@SuppressWarnings("serial")
@Entity
@Data
@Table(name = "bril")
public class Bril implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "imageContent")
    @Lob
    private byte[] image;
  
    // Add additional fields and methods as needed
}
