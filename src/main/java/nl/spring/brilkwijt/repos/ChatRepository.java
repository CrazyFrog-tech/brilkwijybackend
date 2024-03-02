package nl.spring.brilkwijt.repos;

import nl.spring.brilkwijt.repos.dto.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findChatByName(String name);
    Chat findChatById(long id);

    List<Chat> findAll(); // This method fetches all chats with their associated messages

}
