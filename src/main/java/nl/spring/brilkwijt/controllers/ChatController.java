package nl.spring.brilkwijt.controllers;

import nl.spring.brilkwijt.repos.BrilRepository;
import nl.spring.brilkwijt.repos.ChatRepository;
import nl.spring.brilkwijt.repos.MessageRepository;
import nl.spring.brilkwijt.repos.dto.Chat;
import nl.spring.brilkwijt.repos.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("")
public class ChatController {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatRepository chatRepository;

    @MessageMapping("/chat/{to}") //to = nome canale
    public void sendMessage(@DestinationVariable String to , Message message) {
        System.out.println("handling send message: " + message + " to: " + to);
        message.setChat(createAndOrGetChat(to));
        message.setT_stamp(generateTimeStamp());
        message = messageRepository.save(message);
        simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
    }

    @GetMapping("/getChats")
    public List<Chat> getChats(){
        return chatRepository.findAll();
    }

    @PostMapping("/getMessages")
    public List<Message> getMessages(@RequestBody String chat) {
        Chat ce = chatRepository.findChatByName(chat);
        if(ce != null) {
            return messageRepository.findAllByChat_id(ce.getId());
        }
        else{
            return new ArrayList<Message>();
        }
    }

    @PostMapping("/getChatMessages")
    public List<Message> getChatMessages(@RequestBody String id) {
        long idToLong = Long.parseLong(id);
        Chat ce = chatRepository.findChatById(idToLong);
        if(ce != null) {
            return messageRepository.findAllByChat_id(ce.getId());
        }
        else{
            return new ArrayList<Message>();
        }
    }
    //finds the chat whose name is the parameter, if it doesn't exist it gets created, the ID gets returned either way
    private Chat createAndOrGetChat(String name) {
        Chat ce = chatRepository.findChatByName(name);
        if (ce != null) {
            return ce;
        }
        else {
            // name of the chat is not being inserted correctly
            Chat newChat = new Chat(name);
            return chatRepository.save(newChat);
        }
    }

    private String generateTimeStamp() {
        Instant i = Instant.now();
        String date = i.toString();
        System.out.println("Source: " + i.toString());
        int endRange = date.indexOf('T');
        date = date.substring(0, endRange);
        date = date.replace('-', '/');
        System.out.println("Date extracted: " + date);
        String time = Integer.toString(i.atZone(ZoneOffset.UTC).getHour() + 1);
        time += ":";

        int minutes = i.atZone(ZoneOffset.UTC).getMinute();
        if (minutes > 9) {
            time += Integer.toString(minutes);
        } else {
            time += "0" + Integer.toString(minutes);
        }

        System.out.println("Time extracted: " + time);
        String timeStamp = date + "-" + time;
        return timeStamp;
    }



}
