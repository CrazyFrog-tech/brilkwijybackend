package nl.spring.brilkwijt.repos;

import nl.spring.brilkwijt.repos.dto.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findChatByName(String name);
}
