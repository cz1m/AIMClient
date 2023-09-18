package com.like4u.aim.service;

import com.like4u.aim.domain.MessageType;
import com.like4u.aim.domain.Message;
import com.like4u.aim.pojo.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/15 20:04
 */
@Component
public class UserClientService {
    private User user=new User();


    public boolean check(String username,String password)  {
        boolean flag=false;
        try {
            Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            user.setUsername(username);
            user.setPassword(password);
            ObjectOutputStream oos= new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);



            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message msg = (Message)ois.readObject();

            if (msg.getMessageType().equals(MessageType.MESSAGE_LOGIN_SUCCESS)){
                flag=true;

                /**
                 * 创建一个线程来读取socket里面服务器发来的信息
                 * */
                ClientConnectServerThread clientConnectServerThread =
                        new ClientConnectServerThread(socket);
                Thread thread = new Thread(clientConnectServerThread);
                thread.start();

                ManageClientConnectServerThread.addClientConnectServerThread(username,clientConnectServerThread);



            }else {
                ois.close();
                oos.close();

                socket.close();
            }

            return flag;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    public void getUserList(String username){
        try {
            ObjectOutputStream outputStream =new ObjectOutputStream(
                    ManageClientConnectServerThread
                            .getClientConnectServerThread(
                                    username).getSocket().getOutputStream()) ;
            Message getLogin = Message.success(MessageType.MESSAGE_GET_USER_LIST);
            getLogin.setSender(username);
            outputStream.writeObject(getLogin);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void logout(String username){
        ObjectOutputStream outputStream=null;
        try {
             outputStream=new ObjectOutputStream(
                    ManageClientConnectServerThread
                            .getClientConnectServerThread(
                                    username).getSocket().getOutputStream()) ;
            Message exit = Message.success(MessageType.MESSAGE_CLIENT_EXIT);
            exit.setSender(username);
            outputStream.writeObject(exit);
            ManageClientConnectServerThread.getClientConnectServerThread(username).setLoop(false);
            ManageClientConnectServerThread.deleteClientThread(username);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if (outputStream!=null){
                try {
                    outputStream.close();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (ManageClientConnectServerThread.threads==null||ManageClientConnectServerThread.threads.size()==0){
            System.exit(0);
        }

    }

}
