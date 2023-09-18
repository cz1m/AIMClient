package com.like4u.aim.service;

import com.like4u.aim.domain.Message;
import com.like4u.aim.domain.MessageType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/17 17:20
 * 发消息的服务
 */

@Service
public class MessageClientService {
    public void sendMessageToOne(String sender ,String content,String getter){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月dd号-HH:mm:ss");
        String formatTime = dtf.format(now);

        Message message = new Message(sender, getter,content, formatTime, MessageType.MESSAGE_COMMON);

        try {
            ObjectOutputStream oos = ManageClientConnectServerThread.getOOSByUsername(sender);
            oos.writeObject(message);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void sendMessageToAll(String sender,String content){


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月dd号-HH:mm:ss");
        String formatTime = dtf.format(now);

        Message message = new Message(sender, null,content, formatTime, MessageType.MESSAGE_SEND_TO_ALL);

        try {
            ObjectOutputStream oos = ManageClientConnectServerThread.getOOSByUsername(sender);
            oos.writeObject(message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
