package com.like4u.aim.service;

import com.like4u.aim.domain.Message;
import com.like4u.aim.domain.MessageType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/15 20:32
 */
@Data
@Slf4j
public class ClientConnectServerThread implements Runnable{
    private Socket socket;
    private Message message=null;
    private Message commonMessage=null;

    private boolean loop=true;



    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (loop){
            try {
                //System.out.println("等待读取服务器发送消息");

                if (socket.getInputStream().available() > 0){
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    Message msg = (Message)ois.readObject();
                    if (msg.getMessageType().equals(MessageType.MESSAGE_USER_LIST)){

                        this.message=msg;
                    }else if (msg.getMessageType().equals(MessageType.MESSAGE_COMMON)){

                        System.out.println("收到来自"+msg.getSender()+"的消息,内容"+msg.getContent());
                        this.commonMessage=msg;

                       // messages.add(msg);
                    } else if (msg.getMessageType().equals(MessageType.MESSAGE_SEND_TO_ALL)) {
                        System.out.println("收到来自"+msg.getSender()+"的广播");
                        this.commonMessage=msg;
                    } else if (msg.getMessageType().equals(MessageType.MESSAGE_FILE)) {
                        log.info("{}收到来自{}的文件",msg.getGetter(),msg.getSender());
                        FileClientService.saveFile(msg.getDust(),msg.getFileBytes());
                    }


                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
