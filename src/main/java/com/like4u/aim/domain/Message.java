package com.like4u.aim.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/15 16:48
 */
@Data
@TableName(value = "message")
public class Message implements Serializable {
    private static final long serialVersionUID=1L;
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String sender;
    private String getter;
    private String content;
    private String time;
    @TableField(exist = false)
    private Integer MessageType;
    /**
     * 文件的二进制字节数组
     * */
    private byte[] fileBytes;
    /**
     * 文件长度
     * */
    private Integer fileLen;
    /**
     * 源文件路径
     * */
    private String src;
    /**
     * 传输的目标路径
     * */
    private String dust;

    public Message(String content, Integer messageType) {
        this.content = content;
        MessageType = messageType;
    }

    public static Message success(Integer messageType) {

        return success(null, messageType);
    }

    public static Message success(String content, Integer messageType) {
        return new Message(content, messageType);
    }

    public static Message success(String content) {
        return success(content, com.like4u.aim.domain.MessageType.MESSAGE_LOGIN_SUCCESS);
    }

    public Message(String sender, String getter, String content, String time, Integer messageType) {
        this.sender = sender;
        this.getter = getter;
        this.content = content;
        this.time = time;
        MessageType = messageType;
    }

    public Message(String sender, String getter, String time, Integer messageType) {
        this.sender = sender;
        this.getter = getter;
        this.time = time;
        MessageType = messageType;
    }

    public static Message success() {

        return success(com.like4u.aim.domain.MessageType.MESSAGE_LOGIN_SUCCESS);
    }

    public static Message error() {
        return error(null, com.like4u.aim.domain.MessageType.MESSAGE_LOGIN_FAIL);
    }

    public static Message error(String content) {
        return error(content, com.like4u.aim.domain.MessageType.MESSAGE_LOGIN_FAIL);
    }

    public static Message error(String content, Integer messageType) {
        return new Message(content, messageType);

    }

}
