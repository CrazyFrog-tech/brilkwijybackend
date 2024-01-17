package nl.spring.brilkwijt.repos;

import nl.spring.brilkwijt.repos.dto.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChat_id(long chatId);
}
