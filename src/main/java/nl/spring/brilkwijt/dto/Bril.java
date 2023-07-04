package nl.spring.brilkwijt.dto;


import java.io.Serializable;
import java.util.Date;

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
    @Column(name = "description")
    private String description;
    @Column(name = "lostAt")
    private Date lostAtDate;


//    @Column(name = "imageContent")
//    @Lob
//    private byte[] image;
//
//    @Column(name = "lostDate")
//    private Date lostDate;

    // Add additional fields and methods as needed
}
