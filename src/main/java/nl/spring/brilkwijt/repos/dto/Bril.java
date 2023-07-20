package nl.spring.brilkwijt.repos.dto;


import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@Table(name = "bril")
public class Bril implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description")
    private String description;
    @Column(name = "lostAt")
    private Date lostAtDate;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    @OneToMany(mappedBy = "bril")
    private Set<Image> images;


//    @Column(name = "imageContent")
//    @Lob
//    private byte[] image;
//
//    @Column(name = "lostDate")
//    private Date lostDate;

    // Add additional fields and methods as needed
}
