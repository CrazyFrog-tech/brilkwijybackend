package nl.spring.brilkwijt.repos.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "chat")
    @JsonIgnore
    private Set<Message> messages;

    public Chat(String name) {
    }

    public Chat() {

    }
}
