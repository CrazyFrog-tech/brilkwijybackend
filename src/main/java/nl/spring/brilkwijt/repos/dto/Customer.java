package nl.spring.brilkwijt.repos.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "customerName")
    private String customerName;
    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private Set<Bril> brillen;


}
