package com.like4u.aim.service;

import com.like4u.aim.config.CommonConfig;
import com.like4u.aim.domain.Message;
import com.like4u.aim.domain.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/19 13:27
 */
@Slf4j
@Service
public class FileClientService {

    public void sendFileToOne(String sender,String getter,String src){

        String dust = getDust(src);

        Message message = Message.success(MessageType.MESSAGE_FILE);
        message.setSender(sender);
        message.setGetter(getter);
        message.setSrc(src);
        message.setDust(dust);
        message.setFileBytes(loadFile(src));

        log.info("{}向{}发送了一个文件，路径为{}，目标路径为{}",sender,getter,src,message.getDust());

        ObjectOutputStream oos = ManageClientConnectServerThread.getOOSByUsername(sender);
        try {
            oos.writeObject(message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static void saveFile(String src,byte [] bytes){
        BufferedOutputStream bos=null;
        try {
             bos = new BufferedOutputStream(new FileOutputStream(src));
            bos.write(bytes);
        } catch (IOException e) {
            log.info("文件向{}写出失败",src);
            throw new RuntimeException(e);
        }finally {
            try {
                if (bos!=null){
                    bos.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public static String getDust(String src){

        String[] split = src.split("\\\\");
        String fileName = split[split.length - 1];

        return CommonConfig.DOWNLOAD_PATH + fileName;
    }
    public static byte[] loadFile(String src) {

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(src));


            int len = 0;
            byte[] bytes = new byte[bis.available()];
            int read = bis.read(bytes);
            return bytes;

        } catch (IOException e) {
            log.info("文件读取失败，该路径文件不存在");
            throw new RuntimeException(e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public boolean isFileExit(String src){

        File file = new File(src);
        return file.exists();

    }


}
