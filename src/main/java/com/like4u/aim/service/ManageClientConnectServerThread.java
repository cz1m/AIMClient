package com.like4u.aim.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/15 20:54
 */
public class ManageClientConnectServerThread  {
    public static HashMap<String,ClientConnectServerThread> threads=new HashMap<>();

    public  static void addClientConnectServerThread(String username,ClientConnectServerThread clientConnectServerThread){
        threads.put(username,clientConnectServerThread);

 }
    public static ClientConnectServerThread getClientConnectServerThread(String username){
        return threads.get(username);
    }
    public static void deleteClientThread(String username){
        threads.remove(username);
    }

    public static ObjectOutputStream getOOSByUsername(String username){
        try {
            return new ObjectOutputStream(ManageClientConnectServerThread
                    .getClientConnectServerThread(username).getSocket().getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static ObjectInputStream getOISByUsername(String username){
        try {
            return  new ObjectInputStream(ManageClientConnectServerThread
                    .getClientConnectServerThread(username).getSocket().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
